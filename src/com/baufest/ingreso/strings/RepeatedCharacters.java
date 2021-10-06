package com.baufest.ingreso.strings;

import java.util.ArrayList;
import java.util.Arrays;

public class RepeatedCharacters {

    /**
     * El metodo debe retornar un booleano indicando si el parametro `cadena` cumple con alguna de las siguientes propiedades:
     * 1- Todos los caracteres aparecen la misma cantidad de veces.
     *     Ejemplos: "aabbcc", "abcdef", "aaaaaa"
     * 2- Todos los caracteres aparecen la misma cantidad de veces, a excepcion de 1, que aparece un vez mas o una vez menos.
     *     Ejemplos: "aabbccc", "aabbc", "aaaaccccc"
     * @param cadena la cadena a evaluar
     * @return booleano indicando si la cadena cumple con las propiedades
     */    
     public Boolean isValid(String cadena) {
    	Integer inicial = obtenerCantidadRepetidasDeSiguienteLetra(cadena);
    	cadena = filtrarPrimeraLetraDeCadena(cadena);
    	ArrayList<Integer> cantidadRepeticionesObtenidas = new ArrayList<Integer>();
    	cantidadRepeticionesObtenidas.add(inicial);
    	while(cadena.length()>0) {
    		cantidadRepeticionesObtenidas.add(obtenerCantidadRepetidasDeSiguienteLetra(cadena));
    		cadena = filtrarPrimeraLetraDeCadena(cadena);
    	}
    	return verificarCondicionesDeRepeticion(cantidadRepeticionesObtenidas, inicial);
    }
    
     private Boolean verificarCondicionesDeRepeticion(ArrayList<Integer> cantidadRepeticionesObtenidas, Integer inicial) {
    	 for(int i=0; i<cantidadRepeticionesObtenidas.size(); i++) {
     		Integer resta = inicial-cantidadRepeticionesObtenidas.get(i);
     		if(Math.abs(resta)>1) {
     			return false;
     		}
     	}
     	return true;
     }
     
    private int obtenerCantidadRepetidasDeSiguienteLetra(String cadena) {
    	char charToFind = cadena.charAt(0);
    	return countChar(cadena, charToFind);
    }
    private String filtrarPrimeraLetraDeCadena(String cadena) {
    	char charToFind = cadena.charAt(0);
    	return cadena.replaceAll(String.valueOf(charToFind), "");
    }
    
    private int countChar(String cadena, char letra){
        int contador = 0;
        for (int i = 0; i < cadena.length(); i++) {
            if (cadena.charAt(i) == letra) {
                contador++;
            }
        }
        return contador;
    }
    

}
