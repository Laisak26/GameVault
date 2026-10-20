package com.example.GameVault.controllers;

import com.example.GameVault.models.Juego;
import com.example.GameVault.service.JuegoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.example.GameVault.models.Usuario;
import com.example.GameVault.service.UsuarioService;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class GameController {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private JuegoService juegoService;

	@GetMapping("/fragments-demo")
	public String fragmentsDemo() {
		return "fragments-demo";
	}

	@GetMapping({"/", "/juegos"})
	public String listarJuegos(
			@RequestParam(name = "buscar", required = false) String buscar,
			Model model) {

		if (buscar != null && !buscar.isEmpty()) {
			model.addAttribute("juegos", juegoService.buscarPorTitulo(buscar));
			model.addAttribute("busquedaActual", buscar);
		} else {
			model.addAttribute("juegos", juegoService.obtenerTodos());
		}

		return "juegos";
	}

	@GetMapping("/juegos/nuevo")
	public String mostrarFormulario() {
		return "formulario";
	}

	@PostMapping("/juegos")
	public String guardarJuego(@RequestParam("titulo") String titulo,
							   @RequestParam("descripcion") String descripcion,
							   @RequestParam("precio") Double precio,
							   @RequestParam("categoria") String categoria,
							   @RequestParam("portada") MultipartFile portada) {

		Juego nuevoJuego = new Juego();
		nuevoJuego.setTitulo(titulo);
		nuevoJuego.setDescripcion(descripcion);
		nuevoJuego.setPrecio(precio);
		nuevoJuego.setCategoria(categoria);
		nuevoJuego.setUsuario(usuarioService.obtenerUsuarioLogueado());

		juegoService.guardarJuego(nuevoJuego, portada);

		return "redirect:/mis-juegos";
	}

	@GetMapping("/mis-juegos")
	public String misJuegos(Model model) {

		Usuario usuario = usuarioService.obtenerUsuarioLogueado();

		model.addAttribute("usuario", usuario);
		model.addAttribute("juegos", juegoService.obtenerJuegosPorUsuario(usuario.getId()));

		return "mis-juegos";
	}

	@GetMapping("/juegos/editar/{id}")
	public String editarJuego(@PathVariable Long id, Model model) {

		Juego juego = juegoService.obtenerJuegoPorId(id);
		Usuario logueado = usuarioService.obtenerUsuarioLogueado();

		if (juego != null
				&& juego.getUsuario() != null
				&& juego.getUsuario().getId().equals(logueado.getId())) {

			model.addAttribute("juego", juego);
			return "formulario";
		}

		return "redirect:/mis-juegos";
	}

	@PostMapping("/juegos/editar/{id}")
	public String actualizarJuego(@PathVariable Long id,
								  @RequestParam("titulo") String titulo,
								  @RequestParam("descripcion") String descripcion,
								  @RequestParam("precio") Double precio,
								  @RequestParam("categoria") String categoria,
								  @RequestParam(value = "portada", required = false) MultipartFile portada) {

		Juego juegoExistente = juegoService.obtenerJuegoPorId(id);
		Usuario logueado = usuarioService.obtenerUsuarioLogueado();

		if (juegoExistente != null
				&& juegoExistente.getUsuario() != null
				&& juegoExistente.getUsuario().getId().equals(logueado.getId())) {

			juegoExistente.setTitulo(titulo);
			juegoExistente.setDescripcion(descripcion);
			juegoExistente.setPrecio(precio);
			juegoExistente.setCategoria(categoria);

			juegoService.guardarJuego(juegoExistente, portada);
		}

		return "redirect:/mis-juegos";
	}

	@PostMapping("/juegos/eliminar/{id}")
	public String eliminarJuego(@PathVariable Long id) {

		Juego juego = juegoService.obtenerJuegoPorId(id);
		Usuario logueado = usuarioService.obtenerUsuarioLogueado();

		if (juego != null
				&& juego.getUsuario() != null
				&& juego.getUsuario().getId().equals(logueado.getId())) {

			juegoService.eliminarJuego(id);
		}

		return "redirect:/mis-juegos";
	}

}