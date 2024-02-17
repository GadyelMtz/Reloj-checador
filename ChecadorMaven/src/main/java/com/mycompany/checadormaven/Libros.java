/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.checadormaven;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ComparisonOperator;
import org.apache.poi.ss.usermodel.ConditionalFormattingRule;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PatternFormatting;
import org.apache.poi.ss.usermodel.SheetConditionalFormatting;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Libros {

    public Libros() {
    }

    public XSSFWorkbook crearLibro() {
        // Sistema de archivos
        FileSystemView v = FileSystemView.getFileSystemView();
        File archivo = new File(v.getHomeDirectory() + "/RegistroChecador.xlsx");
        XSSFWorkbook libro;

        // Obtener la fecha actual
        String fecha = LocalDate.now().toString();

        try {
            // Si el archivo no existe, crea un nuevo archivo con el encabezado
            if (!archivo.exists()) {
                libro = new XSSFWorkbook();
                crearHoja(libro, fecha);

                try (FileOutputStream out = new FileOutputStream(archivo)) {
                    libro.write(out);
                }

                return libro;
            } else { // Si el archivo existe
                try (FileInputStream fis = new FileInputStream(archivo)) {
                    libro = new XSSFWorkbook(fis);
                    crearHoja(libro, fecha);
                }

                return libro;
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ocurrió un error al crear el libro", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public XSSFSheet crearHoja(XSSFWorkbook libro, String nombre) {
        XSSFSheet hoja = libro.getSheet(nombre);
        if (hoja == null) {
            System.out.println("La hoja no existe, creando hoja");
            hoja = libro.createSheet(nombre);

            XSSFRow renglonEncabezado = hoja.createRow(0);

            // Generar celda de numero de control
            XSSFCell celdaNControl = renglonEncabezado.createCell(0);
            celdaNControl.setCellValue("No. Control");

            // Generar celda de numero de control
            XSSFCell celdaHora = renglonEncabezado.createCell(1);
            celdaHora.setCellValue("Hora");

            // Generar celda de numero de control
            XSSFCell celdaEstatus = renglonEncabezado.createCell(2);
            celdaEstatus.setCellValue("Estatus");
        } else {
            System.out.println("La hoja ya existe, retornando hoja");
        }
        return hoja;
    }

    public boolean escribirHoja(XSSFWorkbook libro, String h, String NControl) {
        // Sistema de archivos
        FileSystemView v = FileSystemView.getFileSystemView();
        File archivo = new File(v.getHomeDirectory() + "/RegistroChecador.xlsx");

        XSSFSheet hoja = libro.getSheet(h);

        // Obtener la cantidad de renglones
        int renglonesAnterior = hoja.getLastRowNum();

        // Generar un nuevo renglon
        XSSFRow renglonNuevo = hoja.createRow(renglonesAnterior + 1);

        // Generar celda de numero de control
        XSSFCell celdaNControl = renglonNuevo.createCell(0);
        // Insertar numero de control
        celdaNControl.setCellValue(NControl);

        // Generar celda de hora
        XSSFCell celdaHora = renglonNuevo.createCell(1);
        LocalTime horaActual = LocalTime.now();
        String horaFormateada = String.format("%02d:%02d:%02d", horaActual.getHour(), horaActual.getMinute(), horaActual.getSecond());
        celdaHora.setCellValue(horaFormateada);

        // Aplicar formato de hora a la celda
        CellStyle cellStyle = libro.createCellStyle();
        celdaHora.setCellStyle(cellStyle);

        // Generar celda de estatus (Con multa o Sin multa)
        XSSFCell celdaEstatus = renglonNuevo.createCell(2);
        if (horaActual.isAfter(LocalTime.of(7, 0))) {
            celdaEstatus.setCellValue("Con multa");
        } else {
            celdaEstatus.setCellValue("Sin multa");
        }

        // Aplicar formato condicional
        SheetConditionalFormatting cfSheet = hoja.getSheetConditionalFormatting();

        // Asegurarse de que haya al menos dos filas para aplicar formato condicional
        if (renglonesAnterior >= 1) {
            // Crear una regla de formato condicional para "Con multa" (color rojo)
            ConditionalFormattingRule ruleConMulta = cfSheet.createConditionalFormattingRule(ComparisonOperator.EQUAL, "\"Con multa\"");
            PatternFormatting fillPatternConMulta = ruleConMulta.createPatternFormatting();
            fillPatternConMulta.setFillBackgroundColor(IndexedColors.RED1.index);
            fillPatternConMulta.setFillPattern(PatternFormatting.SOLID_FOREGROUND);

            // Aplicar las reglas de formato condicional a la columna de estatus (columna 3)
            CellRangeAddress[] regions = {CellRangeAddress.valueOf("C2:C" + (renglonesAnterior + 2))};
            cfSheet.addConditionalFormatting(regions, ruleConMulta);
        }

        try {
            // Genera el archivo y cierra el archivo
            FileOutputStream out = new FileOutputStream(archivo);
            libro.write(out);
            out.close();
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ocurrió un error al escribir en la hoja", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

}
