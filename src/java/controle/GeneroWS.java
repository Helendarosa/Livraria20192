/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import dao.GeneroDAO;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.Genero;


/**
 *
 * @author Aluno
 */
@WebServlet(name = "GeneroWS", urlPatterns = {"/admin/genero/GeneroWS"})
public class GeneroWS extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String acao = request.getParameter("txtAcao");
        RequestDispatcher destino;
        String pagina;
        GeneroDAO dao = new GeneroDAO();
        Genero obj;
        List<Genero> generos;
        Boolean deucerto;
        String msg;
        
        switch(String.valueOf(acao)){
            case "add": // abrir tela e talvez buscar dados
                pagina = "add.jsp";
                break;
                
            case "edit": // abrir a tela e buscar dados
                obj = dao.buscarPorChavePrimaria(Long.parseLong(request.getParameter("txtId")));
                request.setAttribute("obj", obj);
                pagina = "edit.jsp";
                break;
                
            case "del": //excluir dados e buscar dados
               obj = dao.buscarPorChavePrimaria(Long.parseLong(request.getParameter("txtId")));
               deucerto = dao.excluir(obj);
               
               if(deucerto){
                   msg = obj.getNome() + " deletado com sucesso!";
               }
               else{
                   msg = "Problemas ao excluir o gênero " + obj.getNome() + "!";
               }
               
               generos = dao.listar(); // buscar lista atualizada
               request.setAttribute("msg", msg);
               request.setAttribute ("lista", generos);
               pagina = "list.jsp";
               break;
        
            default:
                String filtro = request.getParameter("filtro");
                if (filtro == null){ //listar todos
                   generos = dao.listar();
                }
                else{        //listar com filtro
                   try{      // tipo um if pra erros
                      generos = dao.listar(filtro);
                   } 
                   catch(Exception ex){
                      Logger.getLogger(GeneroWS.class.getName()).log(Level.SEVERE, null, ex);
                      /* Forma de tratar o erro(o erro seria o filtro estar em branco, 
                      o que não aconteceria pois o if é para executar o filtro apenas se ele for diferente de nulo):
                      generos = dao.listar();
                      msg = "Problemas ao filtrar";
                      request.setAttribute ("msg", msg);*/
                   }
                }
                pagina = "list.jsp";
                break;
        }
        destino = request.getRequestDispatcher(pagina);
        destino.forward(request, response);
        
    }

   
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Genero obj; //Receber dados
        GeneroDAO dao = new GeneroDAO();
        Boolean deucerto;
        String msg;
        String pagina;
        RequestDispatcher destino;
        List <Genero> generos;
        
        String id = request.getParameter ("txtId"); //Tratar os dados (transformar os dados no formato solicitado)
        String nome = request.getParameter("txtGenero");
        
        if (id != null){
            obj = dao.buscarPorChavePrimaria(Long.parseLong(id));
        }
        else{
            obj = new Genero();
        }
        
        obj.setNome (nome); //adicionar dados recebidos
        
        if(id!=null){
            deucerto = dao.alterar(obj);
            pagina = "list.jsp";
            generos = dao.listar();
            request.setAttribute("lista", generos);
                if(deucerto){
                    msg = "Gênero alterado com sucesso!";
                }
                else{
                    msg = "Problema ao editar " + obj.getNome() + "!";
                }
        }
        else{
            deucerto = dao.incluir(obj);
            pagina = "add.jsp";
                 if(deucerto){
                    msg = "Gênero adicionado com sucesso!";
                 }
                 else{
                     msg = "Problema ao adicionar " + obj.getNome() + "!";
                 }
        }
        request.setAttribute("msg", msg);
        destino = request.getRequestDispatcher(pagina);
        destino.forward(request, response);
    }

  }
