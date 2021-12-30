package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.AulaDAO;
import model.Aula;
import model.Usuario;

/**
 * Servlet implementation class AulaController
 */
public class AulaController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	AulaDAO aDao = new AulaDAO();
	
	RequestDispatcher rd;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AulaController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		
		HttpSession session = request.getSession();
		Usuario usuario = (Usuario)session.getAttribute("user");
		
		if(usuario == null) {
			response.sendRedirect(request.getContextPath());
			return;
		}
		
		String action = request.getParameter("action");
		
		if (action.equals("reservar")) {
			reservar(request,response);
		}
		else if(action.equals("listar")) {
			listar(request,response);
		}
		else if(action.equals("eliminar")) {
			eliminar(request,response);
		}
		else if(action.equals("ver")) {
			verDetalle(request,response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String action = request.getParameter("action");
		
		if (action.equals("listar")) {
			listar(request,response);
		}
		else if(action.equals("editar")) {
			editar(request,response);
		}
		else if(action.equals("alta")) {
			crear(request,response);
		}
	}
	
	protected void listar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String queryParam = request.getParameter("query");
		List<Aula> aulas = aDao.getAulas(queryParam);
		request.setAttribute("aulas", aulas);
		rd = request.getRequestDispatcher("/main.jsp");
		rd.forward(request, response);
		
	}
	
	protected void reservar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int numaula = Integer.parseInt(request.getParameter("numaula"));
		int idusuario = Integer.parseInt(request.getParameter("idusuario"));
		
		boolean reservado = aDao.reservar(numaula, idusuario);
		request.setAttribute("reservado", reservado);
		
		rd = request.getRequestDispatcher("/aula?action=listar");
		rd.forward(request, response);
		
	}
	
	protected void eliminar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int numaula = Integer.parseInt(request.getParameter("numaula"));
		
		boolean eliminado = aDao.eliminar(numaula);
		
		request.setAttribute("eliminado", eliminado);
		
		rd = request.getRequestDispatcher("/aula?action=listar");
		rd.forward(request, response);
	}
	
	protected void verDetalle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int numaula = Integer.parseInt(request.getParameter("numaula"));
		Aula aula = aDao.obtenerAula(numaula);
		
		request.setAttribute("aula", aula);
		
		rd = request.getRequestDispatcher("/detalle.jsp");
		rd.forward(request, response);
		
		
	}
	
	protected void editar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int numaula = Integer.parseInt(request.getParameter("numaula"));
		String nombre = request.getParameter("nombre");
		int capacidad = Integer.parseInt(request.getParameter("capacidad"));
		String descripcion = request.getParameter("descripcion");
		
		boolean editado = aDao.actualizar(numaula, nombre, capacidad, descripcion);
		
		request.setAttribute("editado", editado);
		rd = request.getRequestDispatcher("/aula?action=listar");
		rd.forward(request, response);
	}
	
	protected void crear(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int numaula = Integer.parseInt(request.getParameter("numaula"));
		String nombre = request.getParameter("nombre");
		int capacidad = Integer.parseInt(request.getParameter("capacidad"));
		String descripcion = request.getParameter("descripcion");
		
		boolean creado = aDao.crear(numaula, nombre, capacidad, descripcion);
		
		if (creado) {
			rd = request.getRequestDispatcher("/aula?action=listar");
			rd.forward(request, response);
		}
		else {
			String msg = "Error al dar de alta el aula";
			request.setAttribute("message", msg);
			rd = request.getRequestDispatcher("/detalle.jsp");
			rd.forward(request, response);
		}
	}

}
