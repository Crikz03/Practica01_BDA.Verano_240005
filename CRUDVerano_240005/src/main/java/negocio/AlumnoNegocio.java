/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio;

import dtos.AlumnoDTO;
import entidad.Alumno;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
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
