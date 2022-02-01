package com.afelio.springbootjpa.controllers;

import com.afelio.springbootjpa.models.entity.Usuario;
import com.afelio.springbootjpa.models.service.ClienteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.NoSuchElementException;
import java.util.UUID;

@Controller
public class ClienteController {
    @Autowired
    ClienteService clienteService;
    Logger logger = LoggerFactory.getLogger(ClienteController.class);
    @GetMapping("/clientes")
    public String listarClientes(Usuario usuario, Model model, @RequestParam(name = "page", defaultValue = "0")int page){
        Pageable pageRequest = PageRequest.of(page, 10);
        Page<Usuario> usuarios = clienteService.findAll(pageRequest);
        String pages = String.valueOf(usuarios.getTotalPages());
        logger.info(pages);
        model.addAttribute("usuarios", usuarios);
        usuarios.forEach(user->{
            String str = user.getCreateAt().toString();
            user.setCreateAt(Date.valueOf(str.substring(0, str.lastIndexOf(" "))));
        });
        return "listar";
    }

    @GetMapping("/form")
    public String insertar(Usuario usuario,Model model){
        usuario = new Usuario();
        model.addAttribute("usuario", usuario);
        model.addAttribute("persisted", false);
        return "form";
    }

    @GetMapping("/edit")
    public String edit(@RequestParam Usuario usuario, Model model){
        return "form";
    }

    @PostMapping("/form")
    public String insert(@Valid Usuario usuario,BindingResult result,Model model, @RequestParam(name = "imagePhoto", required = false, defaultValue = " ") MultipartFile multipartFile){

        if(result.hasErrors()){
            logger.info(result.toString());
            return "form";
        }

        usuario.setImageClientPhoto("");

        if(multipartFile !=null || !multipartFile.isEmpty()){
            if(!multipartFile.isEmpty()){

                String uniqueFileName = UUID.randomUUID().toString().concat("_").concat(multipartFile.getOriginalFilename());
                Path rootPath = Path.of("uploads").resolve(uniqueFileName);
                Path rootAbsolutPath = rootPath.toAbsolutePath();

                try {
                    Files.copy(multipartFile.getInputStream(), rootAbsolutPath);
                    usuario.setImageClientPhoto(uniqueFileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        logger.info("image: " + usuario.getImageClientPhoto());
       clienteService.save(usuario);
        usuario= new Usuario();
        model.addAttribute("usuario", usuario);
        model.addAttribute("persisted", true);
        return "form";
    }

    @PostMapping("/update")
    public String update(Usuario usuario, Model model){
      clienteService.save(usuario);
        return "redirect:/clientes";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam Long id){
        clienteService.deleteById(id);
        return "redirect:/clientes";
    }


    @GetMapping("/ver/{id}")
    public String verPerfil(@PathVariable Long id, Model model){
        Usuario user = clienteService.findOne(id);
        if(user==null){
            user = new Usuario();
            model.addAttribute("usuario", user);
            return "form";
        }
        model.addAttribute("user", user);

            return "ver";
        }


        @PostMapping("/updatePhoto")
        public String updatePhoto(@RequestParam Long id, @RequestParam(name = "image") MultipartFile multipartFile){

            String uniqueFileName = UUID.randomUUID().toString().concat("_").concat(multipartFile.getOriginalFilename());
            Path newPhoto = Path.of("uploads").resolve(uniqueFileName);

            Usuario user = clienteService.findOne(id);
            Path deleteRootPath = Path.of("uploads").resolve(user.getImageClientPhoto());

            try {
                if(user.getImageClientPhoto()!=null){
                    if(!user.getImageClientPhoto().isBlank() || !user.getImageClientPhoto().isBlank()){
                        Files.deleteIfExists(deleteRootPath.toAbsolutePath());
                    }
                }else {

                }
                Files.copy(multipartFile.getInputStream(), newPhoto.toAbsolutePath());
                user.setImageClientPhoto(uniqueFileName);
                clienteService.save(user);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "redirect:/ver/".concat(id.toString());
        }



}
