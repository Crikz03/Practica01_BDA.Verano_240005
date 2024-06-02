/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package persistencia;

import entidad.Alumno;
import java.util.List;

/**
 *
 * @author Chris
 */
public interface IAlumnoDAO {

    public Alumno agregar(Alumno alumno) throws PersistenciaException;

    public void actualizarAlumno(Alumno alumno) throws PersistenciaException;

    public void eliminarAlumno(int idAlumno) throws PersistenciaException;

    public void AlumnoInactivo(int idAlumno, boolean estado) throws PersistenciaException;

    public List<Alumno> buscarAlumno(int limit, int offset) throws PersistenciaException;

    public Alumno buscarAlumnoPorId(int idAlumno) throws PersistenciaException;
}
