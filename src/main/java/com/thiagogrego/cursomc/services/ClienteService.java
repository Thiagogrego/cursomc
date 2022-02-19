package com.thiagogrego.cursomc.services;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.thiagogrego.cursomc.config.UploadFileConfig;
import com.thiagogrego.cursomc.domain.Cidade;
import com.thiagogrego.cursomc.domain.Cliente;
import com.thiagogrego.cursomc.domain.Endereco;
import com.thiagogrego.cursomc.domain.enums.Perfil;
import com.thiagogrego.cursomc.domain.enums.TipoCliente;
import com.thiagogrego.cursomc.dto.ClienteDTO;
import com.thiagogrego.cursomc.dto.ClienteNewDTO;
import com.thiagogrego.cursomc.repositories.ClienteRepository;
import com.thiagogrego.cursomc.repositories.EnderecoRepository;
import com.thiagogrego.cursomc.security.UserSS;
import com.thiagogrego.cursomc.services.exceptions.AuthorizationException;
import com.thiagogrego.cursomc.services.exceptions.DataIntegrityException;
import com.thiagogrego.cursomc.services.exceptions.FileStorageException;
import com.thiagogrego.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repo;

	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private ImageService imageService;
	
	@Value("${img.prefix.client.profile}")
	private String prefix;
	
	@Value("${img.profile.size}")
	private Integer size;

	public List<Cliente> findAll() {
		return repo.findAll();
	}

	public Cliente findById(Integer id) {
		
		UserSS user = UserService.authenticated();
		if (user==null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso negado");
		}
		
		Optional<Cliente> cliente = repo.findById(id);

		if (cliente.isEmpty()) {
			return cliente.orElseThrow(() -> new ObjectNotFoundException(
					"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
		}
		return cliente.get();
	}
	
	public Cliente findByEmail(String email) {
		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Perfil.ADMIN) && !email.equals(user.getUsername())) {
			throw new AuthorizationException("Acesso negado");
		}

		Cliente obj = repo.findByEmail(email);
		if (obj == null) {
			throw new ObjectNotFoundException(
					"Objeto não encontrado! Id: " + user.getId() + ", Tipo: " + Cliente.class.getName());
		}
		return obj;
	}

	@Transactional
	public Cliente save(Cliente cliente) {
		cliente.setId(null);
		cliente = repo.save(cliente);
		enderecoRepository.saveAll(cliente.getEnderecos());
		return cliente;

	}

	public Cliente update(Cliente cliente) {

		Cliente newCliente = findById(cliente.getId());
		upadateData(newCliente, cliente);
		return repo.save(newCliente);
	}

	public void delete(Integer id) {
		findById(id);
		try {
			repo.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possivel excluir porque há pedidos relacionadas");
		}
	}

	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}

	public Cliente fromDTO(ClienteDTO clienteDTO) {
		return new Cliente(clienteDTO.getId(), clienteDTO.getNome(), clienteDTO.getEmail(), null, null, null);
	}

	public Cliente fromDTO(ClienteNewDTO clienteNewDTO) {
		Cliente cli = new Cliente(null, clienteNewDTO.getNome(), clienteNewDTO.getEmail(), clienteNewDTO.getCpfOuCnpj(),TipoCliente.toEnum(clienteNewDTO.getTipo()), bCryptPasswordEncoder.encode(clienteNewDTO.getSenha()));
		Cidade cid = new Cidade(clienteNewDTO.getCidadeId(), null, null);
		Endereco end = new Endereco(null, clienteNewDTO.getLogradouro(), clienteNewDTO.getNumero(),clienteNewDTO.getComplemento(), clienteNewDTO.getBairro(), clienteNewDTO.getCep(), cli, cid);
		cli.getEnderecos().add(end);
		cli.getTelefones().add(clienteNewDTO.getTelefone1());
		if (clienteNewDTO.getTelefone2() != null) {
			cli.getTelefones().add(clienteNewDTO.getTelefone2());
		}
		if (clienteNewDTO.getTelefone3() != null) {
			cli.getTelefones().add(clienteNewDTO.getTelefone3());
		}
		return cli;
	}

	private void upadateData(Cliente newCliente, Cliente cliente) {
		newCliente.setNome(cliente.getNome());
		newCliente.setEmail(cliente.getEmail());
	}
	
	private final Path fileStorageLocation;

    @Autowired
    public ClienteService(UploadFileConfig uploadFileConfig) {
        this.fileStorageLocation = Paths.get(uploadFileConfig.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
        	
        	UserSS user = UserService.authenticated();
        	if(user == null) {
        		throw new AuthorizationException("Acesso negado");
        	}
        	
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Desculpe! O nome do arquivo contém uma sequência de caminho inválida " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            BufferedImage jpgImage = imageService.getJpgImageFromFile(file);
    		jpgImage = imageService.cropSquare(jpgImage);
    		jpgImage = imageService.resize(jpgImage, size);
            fileName = prefix + user.getId() + ".jpg";
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(imageService.getInputStream(jpgImage, "jpg"), targetLocation, StandardCopyOption.REPLACE_EXISTING); 
            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Não foi possível realizar o upload da imagem", ex);
        }
    }
}
