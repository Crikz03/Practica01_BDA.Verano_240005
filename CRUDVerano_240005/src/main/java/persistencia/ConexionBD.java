/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Chris
 */
public class ConexionBD implements IConexionBD {

    final String SERVER = "localhost";
    final String database = "crud";
    final String conexionBD = "jdbc:mysql://" + SERVER + "/" + database;
    final String usuario = "root";
    final String contraseña = "Itson";

    @Override
    public Connection crearConexion() throws SQLException {
        Connection conexion = DriverManager.getConnection(conexionBD, usuario, contraseña);
        return conexion;
    }
}
