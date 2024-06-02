/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package persistencia;

import dtos.AlumnoDTO;
import entidad.Alumno;
import java.util.List;

/**
 *
 * @author Chris
 */
public interface IAlumnoDAO {
    
    public Alumno agregar (Alumno alumno) throws PersistenciaException;

    public List<Alumno> buscarAlumno(int limit, int offset) throws PersistenciaException;
}
