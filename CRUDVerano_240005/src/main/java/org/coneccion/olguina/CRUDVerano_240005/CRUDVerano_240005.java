/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package org.coneccion.olguina.CRUDVerano_240005;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author lv1822
 */
public class CRUDVerano_240005 {

    public static void main(String[] args) {
        final String SERVER = "localhost";
        final String database = "crud";
        final String conexionBD = "jdbc:mysql://" + SERVER + "/" + database;
        final String usuario = "root";
        final String contraseña = "Itson";

        try {
            Connection conexion = DriverManager.getConnection(conexionBD, usuario, contraseña);

            String sentenciaSQL = "INSERT INTO `alumnos` (`nombres`,`apellidoPaterno`,`apellidoMaterno`) VALUES (?,?,?);";

            PreparedStatement pS = conexion.prepareStatement(sentenciaSQL, Statement.RETURN_GENERATED_KEYS);

            pS.setString(1, "Chris Alberto");
            pS.setString(2, "Elizalde");
            pS.setString(3, "Andrade");

            pS.executeUpdate();

            ResultSet res = pS.getGeneratedKeys();

            while (res.next()) {
                System.out.println(res.getInt(1));
            }
        } catch (SQLException e) {
            System.out.println("Ocurrio un error: " + e.getMessage());
        }

//        try {
//            Connection conexion = DriverManager.getConnection(conexionBD, usuario, contraseña);
//
//            String sSQL = "SELECT * FROM alumnos WHERE idAlumno = ?;";
//
//            PreparedStatement preS = conexion.prepareStatement(sSQL);
//            
//            preS.setInt(1, 1);
//            
//            ResultSet resultado = preS.executeQuery();
//            
//            
//            while (resultado.next()){
//                int idAlumno = resultado.getInt("idAlumno");
//                String nombres = resultado.getString("nombres");
//                String apellidoPaterno = resultado.getString("apellidoPaterno");
//                String apellidoMaterno = resultado.getString("apellidoMaterno");
//                boolean eliminado = resultado.getBoolean("eliminado");
//                boolean activo = resultado.getBoolean("activo");
//                
//                System.out.println("ID del alumno: "+idAlumno);
//                System.out.println("Nombres: "+nombres);
//                System.out.println("Apellido Paterno: "+apellidoPaterno);
//                System.out.println("Apellido Materno: "+apellidoMaterno);
//                System.out.println("Eliminado: "+eliminado);
//                System.out.println("Activo: "+activo);
//            }
//        } catch (SQLException e) {
//            System.out.println("Ocurrio un error: "+ e.getMessage());
//        }
    }
}
