/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package org.coneccion.olguina.CRUDVerano_240005;

import negocio.AlumnoNegocio;
import negocio.IAlumnoNegocio;
import persistencia.AlumnoDAO;
import persistencia.ConexionBD;
import persistencia.IAlumnoDAO;
import persistencia.IConexionBD;
import presentacion.frmCrud;

/**
 *
 * @author lv1822
 */
public class Prueba {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        IConexionBD conexionBD = new ConexionBD();
        IAlumnoDAO alumnosDAO = new AlumnoDAO(conexionBD);

        IAlumnoNegocio alumnoNeg = new AlumnoNegocio(alumnosDAO);

        frmCrud frmCrud = new frmCrud(alumnoNeg);
        frmCrud.show();

    }

}
