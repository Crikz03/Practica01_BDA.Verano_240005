/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio;

import dtos.AlumnoDTO;
import entidad.Alumno;
import java.util.ArrayList;
import java.util.List;
import persistencia.IAlumnoDAO;
import persistencia.PersistenciaException;
import utilerias.Utilidades;

/**
 *
 * @author Chris
 */
public class AlumnoNegocio implements IAlumnoNegocio {

    private IAlumnoDAO alumnoDAO;

    public AlumnoNegocio(IAlumnoDAO alumnoDAO) {
        this.alumnoDAO = alumnoDAO;
    }

    @Override
    public AlumnoDTO agregaAlumno(AlumnoDTO alumnoDTO) throws NegocioException, PersistenciaException {
        Alumno a = new Alumno();
        a.setIdAlumno(alumnoDTO.getIdAlumno());
        a.setNombres(alumnoDTO.getNombres());
        a.setApellidoPaterno(alumnoDTO.getApellidoPaterno());
        a.setApellidoMaterno(alumnoDTO.getApellidoMaterno());
        a.setActivo("activo".equalsIgnoreCase(alumnoDTO.getEstatus()));

        Alumno alumnoGuardado = alumnoDAO.agregar(a);

        AlumnoDTO alumnoDTOResultante = convertirADTO(alumnoGuardado);

        return alumnoDTOResultante;

    }

    @Override
    public void actualizarAlumno(AlumnoDTO alumnoDTO) throws NegocioException, PersistenciaException {
        try {
            Alumno alumno = new Alumno();
            alumno.setIdAlumno(alumnoDTO.getIdAlumno());
            alumno.setNombres(alumnoDTO.getNombres());
            alumno.setApellidoPaterno(alumnoDTO.getApellidoPaterno());
            alumno.setApellidoMaterno(alumnoDTO.getApellidoMaterno());
            alumno.setActivo("activo".equalsIgnoreCase(alumnoDTO.getEstatus()));
            alumno.setEliminado(false);

            alumnoDAO.actualizarAlumno(alumno);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al actualizar el alumno: " + e.getMessage());
        }
    }

    @Override
    public void eliminarAlumno(int idAlumno) throws NegocioException, PersistenciaException {
        try {
            alumnoDAO.eliminarAlumno(idAlumno);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al eliminar el alumno: " + e.getMessage());
        }
    }

    @Override
    public void AlumnoInactivo(int idAlumno, boolean estado) throws NegocioException, PersistenciaException {
        try {
            alumnoDAO.AlumnoInactivo(idAlumno, estado);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al cambiar el estado del alumno: " + e.getMessage());
        }
    }

    @Override
    public AlumnoDTO buscarAlumnoPorId(int idAlumno) throws NegocioException, PersistenciaException {
        try {
            Alumno alumno = alumnoDAO.buscarAlumnoPorId(idAlumno);
            return convertirADTO(alumno);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al buscar el alumno por ID: " + e.getMessage());
        }
    }

    private AlumnoDTO convertirADTO(Alumno alumno) throws NegocioException {
        AlumnoDTO dto = new AlumnoDTO();
        dto.setIdAlumno(alumno.getIdAlumno());
        dto.setNombres(alumno.getNombres());
        dto.setApellidoPaterno(alumno.getApellidoPaterno());
        dto.setApellidoMaterno(alumno.getApellidoMaterno());
        dto.setEstatus(alumno.isActivo() == true ? "activo" : "Inactivo");

        return dto;

    }

    private List<AlumnoDTO> convertirAlumnoDTO(List<Alumno> alumnos) throws NegocioException {
        if (alumnos == null) {
            throw new NegocioException("No se pudieron obtener la lista de los alumnos");
        }
        List<AlumnoDTO> alumnosDTO = new ArrayList<>();
        for (Alumno alumno : alumnos) {
            AlumnoDTO dto = new AlumnoDTO();
            dto.setIdAlumno(alumno.getIdAlumno());
            dto.setNombres(alumno.getNombres());
            dto.setApellidoPaterno(alumno.getApellidoPaterno());
            dto.setApellidoMaterno(alumno.getApellidoMaterno());
            dto.setEstatus(alumno.isActivo() == true ? "activo" : "Inactivo");
            alumnosDTO.add(dto);
        }
        return alumnosDTO;
    }

    @Override
    public List<AlumnoDTO> buscarAlumnos(int limit, int pagina) throws NegocioException {
        try {
            this.esNumeroNegativo(limit);
            this.esNumeroNegativo(pagina);

            int offset = this.obtenerOFFSETMySQL(limit, pagina);

            List<Alumno> listaAlumno = this.alumnoDAO.buscarAlumno(limit, offset);

            if (listaAlumno == null) {
                throw new NegocioException("No existen clientes registrados");
            }
            return this.convertirAlumnoDTO(listaAlumno);
        } catch (PersistenciaException e) {
            System.out.println(e.getMessage());
            throw new NegocioException(e.getMessage());
        }
    }

    private void esNumeroNegativo(int numero) throws NegocioException {
        if (numero < 0) {
            throw new NegocioException("El numero ingresado es negativo bro");
        }
    }

    private int obtenerOFFSETMySQL(int limite, int pagina) throws NegocioException {
        int offset = new Utilidades().RegresarOFFSETMySQL(limite, pagina);
        this.esNumeroNegativo(offset);
        return offset;
    }

}
