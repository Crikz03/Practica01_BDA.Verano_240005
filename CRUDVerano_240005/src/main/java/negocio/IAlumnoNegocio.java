/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package negocio;

import dtos.AlumnoDTO;
import entidad.Alumno;
import java.util.List;
import persistencia.PersistenciaException;

/**
 *
 * @author Chris
 */
public interface IAlumnoNegocio {
    
    public AlumnoDTO agregaAlumno(AlumnoDTO alumno) throws NegocioException, PersistenciaException;
    
    public List<AlumnoDTO> buscarAlumnos(int limite, int pagina)throws NegocioException;
}
