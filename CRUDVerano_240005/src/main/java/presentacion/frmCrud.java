/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package presentacion;

import dtos.AlumnoDTO;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import negocio.IAlumnoNegocio;
import negocio.NegocioException;
import persistencia.PersistenciaException;
import utilerias.JButtonCellEditor;
import utilerias.JButtonRenderer;

/**
 *
 * @author Chris
 */
public class frmCrud extends javax.swing.JFrame {

    private int pagina = 1;
    private final int limite = 2;
    private IAlumnoNegocio alumnoNeg;

    /**
     * Creates new form frmCrud
     */
    public frmCrud(IAlumnoNegocio alumnoNegocio) {
        initComponents();

        this.alumnoNeg = alumnoNegocio;
        this.cargarMetodosIniciales();
    }

    private void cargarMetodosIniciales() {
        this.cargarConfiguracionInicialPantalla();
        this.cargarConfiguracionInicialTablaAlumno();
        this.cargarAlumnosEnTabla();
    }

    private void cargarConfiguracionInicialPantalla() {

    }

    private void cargarConfiguracionInicialTablaAlumno() {
        ActionListener onEditarClickListener = new ActionListener() {
            final int columnaId = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                editar();
            }
        };
        int indiceColumnaEditar = 5;
        TableColumnModel modeloColumnas = this.tblAlumnos.getColumnModel();
        modeloColumnas.getColumn(indiceColumnaEditar).setCellRenderer(new JButtonRenderer("Editar"));
        modeloColumnas.getColumn(indiceColumnaEditar).setCellEditor(new JButtonCellEditor("Editar", onEditarClickListener));

        ActionListener onEstatusInactivoClickListener = new ActionListener() {
            final int columnaId = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                inactivo();
            }
        };
        int indiceColumnaInactivo = 6;
        modeloColumnas = this.tblAlumnos.getColumnModel();
        modeloColumnas.getColumn(indiceColumnaInactivo).setCellRenderer(new JButtonRenderer("Estado"));
        modeloColumnas.getColumn(indiceColumnaInactivo).setCellEditor(new JButtonCellEditor("Estado", onEstatusInactivoClickListener));

        ActionListener onEliminarClickListener = new ActionListener() {
            final int columnald = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                eliminar();
            }
        };

        int indiceColumnaEliminar = 7;
        modeloColumnas = this.tblAlumnos.getColumnModel();
        modeloColumnas.getColumn(indiceColumnaEliminar).setCellRenderer(new JButtonRenderer("Eliminar"));

        modeloColumnas.getColumn(indiceColumnaEliminar).setCellEditor(new JButtonCellEditor("Eliminar", onEliminarClickListener));
    }

    private int getIdSeleccionadoTabla() {
        int indiceFilaSeleccionada = this.tblAlumnos.getSelectedRow();

        if (indiceFilaSeleccionada != -1) {
            DefaultTableModel modelo = (DefaultTableModel) this.tblAlumnos.getModel();
            int indiceColumnaId = 0;
            int idSocioSeleccionado = (int) modelo.getValueAt(indiceFilaSeleccionada, indiceColumnaId);

            return idSocioSeleccionado;
        } else {
            return 0;
        }
    }

    private void editar() {
        int id = this.getIdSeleccionadoTabla();

        try {
            AlumnoDTO alumno = alumnoNeg.buscarAlumnoPorId(id);

            if (alumno != null) {
                JTextField nuevoNombre = new JTextField(alumno.getNombres());
                JTextField nuevoApellidoPaterno = new JTextField(alumno.getApellidoPaterno());
                JTextField nuevoApellidoMaterno = new JTextField(alumno.getApellidoMaterno());

                Object[] message = {
                    "Nuevo Nombre:", nuevoNombre,
                    "Nuevo Apellido Paterno:", nuevoApellidoPaterno,
                    "Nuevo Apellido Materno:", nuevoApellidoMaterno
                };

                int option = JOptionPane.showConfirmDialog(this, message, "Editar Alumno", JOptionPane.OK_CANCEL_OPTION);

                if (option == JOptionPane.OK_OPTION) {
                    alumno.setNombres(nuevoNombre.getText());
                    alumno.setApellidoPaterno(nuevoApellidoPaterno.getText());
                    alumno.setApellidoMaterno(nuevoApellidoMaterno.getText());

                    alumnoNeg.actualizarAlumno(alumno);
                    this.actualizarTabla();
                    JOptionPane.showMessageDialog(this, "Alumno actualizado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Alumno no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NegocioException | PersistenciaException e) {
            Logger.getLogger(frmCrud.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void inactivo() {
        int id = this.getIdSeleccionadoTabla();

        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que deseas cambiar el estado del alumno a inactivo?", "Confirmar Cambio de Estado", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                alumnoNeg.AlumnoInactivo(id, false);
                actualizarTabla();
                JOptionPane.showMessageDialog(this, "Estado del alumno cambiado a inactivo exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (NegocioException | PersistenciaException e) {
                Logger.getLogger(frmCrud.class.getName()).log(Level.SEVERE, null, e);
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminar() {
        int id = this.getIdSeleccionadoTabla();

        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que deseas eliminar al alumno?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                alumnoNeg.eliminarAlumno(id);
                actualizarTabla();
                JOptionPane.showMessageDialog(this, "Alumno eliminado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
            } catch (NegocioException | PersistenciaException e) {
                Logger.getLogger(frmCrud.class.getName()).log(Level.SEVERE, null, e);
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void llenarTablaAlumnos(List<AlumnoDTO> alumnoLista) {
        DefaultTableModel modeloTabla = (DefaultTableModel) this.tblAlumnos.getModel();

        if (modeloTabla.getRowCount() > 0) {
            for (int i = modeloTabla.getRowCount() - 1; i > -1; i--) {
                modeloTabla.removeRow(i);
            }
        }

        if (alumnoLista != null) {
            alumnoLista.forEach(row -> {
                Object[] fila = new Object[5];
                fila[0] = row.getIdAlumno();
                fila[1] = row.getNombres();
                fila[2] = row.getApellidoPaterno();
                fila[3] = row.getApellidoMaterno();
                fila[4] = row.getEstatus();

                modeloTabla.addRow(fila);
            });
        }
    }

    private void actualizarTabla() {
        try {
            List<AlumnoDTO> listaAlumnos = alumnoNeg.buscarAlumnos(limite, pagina);
            DefaultTableModel model = (DefaultTableModel) this.tblAlumnos.getModel();
            model.setRowCount(0);

            for (AlumnoDTO alumno : listaAlumnos) {
                Object[] fila = {
                    alumno.getIdAlumno(),
                    alumno.getNombres(),
                    alumno.getApellidoPaterno(),
                    alumno.getApellidoMaterno(),
                    alumno.getEstatus()
                };
                model.addRow(fila);
            }
        } catch (NegocioException e) {
            Logger.getLogger(frmCrud.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarAlumnosEnTabla() {
        try {
            List<AlumnoDTO> alumnos = this.alumnoNeg.buscarAlumnos(this.limite, this.pagina);
            this.llenarTablaAlumnos(alumnos);
        } catch (NegocioException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Informacion", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtNombres = new javax.swing.JTextField();
        txtApellidoP = new javax.swing.JTextField();
        txtApellidoM = new javax.swing.JTextField();
        bRegistro = new javax.swing.JButton();
        checkBActivo = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAlumnos = new javax.swing.JTable();
        bAtras = new javax.swing.JButton();
        bSiguiente = new javax.swing.JButton();
        lblTituloPaginado = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        jLabel1.setText("Administración de alumnos");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel2.setText("Nombres:");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel3.setText("Apellido Paterno:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel4.setText("Apellido Materno:");

        bRegistro.setText("Nuevo registro");
        bRegistro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bRegistroActionPerformed(evt);
            }
        });

        checkBActivo.setText("Activo");

        tblAlumnos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Nombres", "A. Paterno", "A. Materno", "Estatus", "Editar", "Estado", "Eliminar"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblAlumnos);

        bAtras.setText("Atras");
        bAtras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAtrasActionPerformed(evt);
            }
        });

        bSiguiente.setText("Siguiente");
        bSiguiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bSiguienteActionPerformed(evt);
            }
        });

        lblTituloPaginado.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblTituloPaginado.setText("Página 1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(bRegistro, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(txtNombres, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(txtApellidoP, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(26, 26, 26)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(txtApellidoM, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(28, 28, 28)
                                .addComponent(checkBActivo)
                                .addGap(0, 121, Short.MAX_VALUE))
                            .addComponent(jScrollPane1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(bAtras)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblTituloPaginado)
                                .addGap(380, 380, 380)
                                .addComponent(bSiguiente)))))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNombres, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(checkBActivo)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(txtApellidoM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(txtApellidoP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(12, 12, 12)
                .addComponent(bRegistro)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 391, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bAtras)
                    .addComponent(bSiguiente)
                    .addComponent(lblTituloPaginado))
                .addContainerGap(7, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bRegistroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bRegistroActionPerformed
        AlumnoDTO alumno = new AlumnoDTO();

        alumno.setNombres(txtNombres.getText());
        alumno.setApellidoPaterno(txtApellidoP.getText());
        alumno.setApellidoMaterno(txtApellidoM.getText());

        try {
            alumnoNeg.agregaAlumno(alumno);
            JOptionPane.showMessageDialog(null, "Exito al guardar el alumno");
            limpiarCampos();
        } catch (NegocioException ex) {
            Logger.getLogger(frmCrud.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PersistenciaException ex) {
            Logger.getLogger(frmCrud.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_bRegistroActionPerformed

    private void bSiguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSiguienteActionPerformed
        this.pagina = this.pagina + 1;
        this.cargarAlumnosEnTabla();
        this.lblTituloPaginado.setText("Página " + this.pagina);
    }//GEN-LAST:event_bSiguienteActionPerformed

    private void bAtrasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAtrasActionPerformed
        this.pagina = this.pagina - 1;
        this.cargarAlumnosEnTabla();
        this.lblTituloPaginado.setText("Página " + this.pagina);
    }//GEN-LAST:event_bAtrasActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bAtras;
    private javax.swing.JButton bRegistro;
    private javax.swing.JButton bSiguiente;
    private javax.swing.JCheckBox checkBActivo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblTituloPaginado;
    private javax.swing.JTable tblAlumnos;
    private javax.swing.JTextField txtApellidoM;
    private javax.swing.JTextField txtApellidoP;
    private javax.swing.JTextField txtNombres;
    // End of variables declaration//GEN-END:variables
private void limpiarCampos() {
        txtNombres.setText("");
        txtApellidoP.setText("");
        txtApellidoM.setText("");
    }

}
