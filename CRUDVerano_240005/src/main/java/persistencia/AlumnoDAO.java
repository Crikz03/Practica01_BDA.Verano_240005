/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia;

import dtos.AlumnoDTO;
import entidad.Alumno;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Chris
 */
public class AlumnoDAO implements IAlumnoDAO {

    IConexionBD conexionBD;

    public AlumnoDAO(IConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    @Override
    public Alumno agregar(Alumno alumnoNuevo) throws PersistenciaException {
        String sentenciaSQL = "INSERT INTO `alumnos` (`nombres`,`apellidoPaterno`,`apellidoMaterno`) VALUES (?,?,?);";
        try {
            Connection conexion = this.conexionBD.crearConexion();
            PreparedStatement pS = conexion.prepareStatement(sentenciaSQL, Statement.RETURN_GENERATED_KEYS);

            pS.setString(1, alumnoNuevo.getNombres());
            pS.setString(2, alumnoNuevo.getApellidoPaterno());
            pS.setString(3, alumnoNuevo.getApellidoMaterno());

            pS.executeUpdate();

            ResultSet res = pS.getGeneratedKeys();

            int idGenerado = -1;
            if (res.next()) {
                idGenerado = res.getInt(1);
                alumnoNuevo.setIdAlumno(idGenerado);
            } else {
                throw new PersistenciaException("No se pudo obtener el ID generado.");
            }

            return alumnoNuevo;
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo guardar el cliente.");
        }
    }

    public Alumno buscarAlumnoPorId(int idAlumno) throws PersistenciaException {
        String sentenciaSQL = "SELECT * FROM alumnos WHERE idAlumno = ?;";
        ResultSet res = null;

        try (Connection conexion = this.conexionBD.crearConexion(); PreparedStatement ps = conexion.prepareStatement(sentenciaSQL)) {

            ps.setInt(1, idAlumno);

            res = ps.executeQuery();

            if (res.next()) {
                return convertirAlumno(res);
            } else {
                throw new PersistenciaException("No se encontró el alumno con ID: " + idAlumno);
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error al buscar el alumno por ID: " + e);
        }
    }

    @Override
    public List<Alumno> buscarAlumno(int limit, int offset) throws PersistenciaException {
        List<Alumno> alumnos = new ArrayList<>();
        String sql = "SELECT * FROM alumnos LIMIT ? OFFSET ?";
        try (Connection conexion = this.conexionBD.crearConexion(); PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, limit);
            ps.setInt(2, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Alumno alumno = new Alumno();
                    alumno.setIdAlumno(rs.getInt("idAlumno"));
                    alumno.setNombres(rs.getString("nombres"));
                    alumno.setApellidoPaterno(rs.getString("apellidoPaterno"));
                    alumno.setApellidoMaterno(rs.getString("apellidoMaterno"));
                    alumno.setActivo(rs.getBoolean("activo"));
                    alumnos.add(alumno);
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error al buscar los alumnos.");
        }
        return alumnos;
    }

    private Alumno convertirAlumno(ResultSet resultado) throws SQLException {
        int id = resultado.getInt("idAlumno");
        String nombre = resultado.getString("nombres");
        String paterno = resultado.getString("apellidoPaterno");
        String materno = resultado.getString("apellidoMaterno");
        boolean eliminado = resultado.getBoolean("eliminado");
        boolean activo = resultado.getBoolean("activo");
        return new Alumno(id, nombre, paterno, materno, eliminado, activo);
    }

}
