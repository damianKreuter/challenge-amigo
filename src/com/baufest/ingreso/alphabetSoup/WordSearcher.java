package com.baufest.ingreso.alphabetSoup;

import java.awt.geom.Area;
import java.util.ArrayList;

public class WordSearcher {

	//ES UN LIMITANTE PARA DECIR CUANTOS SENTIDOS TIENE PERMITIDO EL ALGORITMO
	//DE CAMBIAR DE SENTIDO DE LA DIRECCIÓN AL CAMBIAR DE VERTICAL-HORIZONTAL
	//Y ASÍ
	private Integer valorMaximoDeSentidosPermitidos = 4;
	
    private char soup[][];
    
    public WordSearcher(char soup[][]){
        this.soup = soup;
    }

    /**
     * El objetivo de este ejercicio es implementar una función que determine si una palabra está en una sopa de letras.
     *
     * ### Reglas
     * - Las palabras pueden estar dispuestas Sentidoes horizontal o vertical, _no_ en diagonal.
     * - Las palabras pueden estar orientadas en cualquier sentido, esto es, de derecha a izquierda o viceversa, y de arriba
     * para abajo o viceversa.
     * - El cambio de dirección puede estar a media palabra, de modo que, por ejemplo, parte de la palabra
     * esté horizontal y de izquierda a derecha, parte esté vertical y de arriba hacia abajo, y otra parte horizontal
     * de derecha a la izquierda.
     *
     * @param word	Palabra a buscar en la sopa de letras.
     *
     * @return {@link Boolean}	true si la palabra se encuentra
     * en la sopa de letras.
     * */
    public boolean isPresent(String word){
        //TODO: resolver
    	
    	/*
    	 * PASOS
    	 * 1* Con la primera letra, buscar cualquier candidato que haga MATCH
    	 * 2* Explorar a los 4 lados (arriba, abajo, izquierda y derecha) para encontrar la siguiente letra
    	 * 3* Explorar en las siguientes 3 Sentidoes la siguiente letra descartando de donde ha venido
    	 * 		EJ, si vino de la izquierda no puede ir a Derecha, lo mismo  si va hacia Arriba no puede bajar
    	 * 4* Si falla, debe retornar para atrás y fijarse en las siguientes opciones descartando por donde vino
    	 * 5* Si no se logra encontrar nada estamos ante una falla y dará FALSE
    	 */
        return encontrarPalabra(word);
    }
    
    private Boolean encontrarPalabra(String palabra) {
    	
    	ArrayList<Integer> posicionDeUltimaPrimerLetra = new ArrayList<Integer>();
    	posicionDeUltimaPrimerLetra.add(0);
    	posicionDeUltimaPrimerLetra.add(0);
    	
    	Boolean encontrado = false;
    	try {
    		while(!encontrado) {
    			ArrayList<Sentido> sentidosPermitidos = new ArrayList<Sentido>();
    			ArrayList<Integer> posicionDePrimerPalabra = buscarPrimeraLetra(palabra.charAt(0), posicionDeUltimaPrimerLetra);
    			//UNA VEZ ENCONTRADA LA PRIMERA POSICION, HAY QUE BUSCAR EN LAS 4 SentidoES
    			String cadenaAFormar = ""+palabra.charAt(0);
    			encontrado = buscarSiguienteLetra(palabra, 1, posicionDePrimerPalabra, Sentido.INICIO, cadenaAFormar, sentidosPermitidos);
    			posicionDeUltimaPrimerLetra = pasarSiguienteCelda(posicionDeUltimaPrimerLetra);
    		}
    	} catch (ExcepcionNoHayMasEspacioEnSopa e) {
			return false;
		}catch (Exception e) {
			//NO SE LOGRÓ ENCONTRAR LA PALABRA BUSCADA
			return false;
		}
    	return encontrado;
    }
    
    private ArrayList<Integer> pasarSiguienteCelda(ArrayList<Integer> posicionOriginal) throws ExcepcionNoHayMasEspacioEnSopa {
    	int x = posicionOriginal.get(0);
    	int y = posicionOriginal.get(1);
    	if(x+1<soup.length) {
    		x+=1;
    	} else if(x==soup.length && y+1>soup[0].length) {
    			//NOS HEMOS SOBREPASADO, DEBEMOS DAR UNA EXCEPCION
    			throw new ExcepcionNoHayMasEspacioEnSopa();
    		} else {
    			x=0;
    			y+=1;
    		}
    	ArrayList<Integer> posicionNueva = new ArrayList<Integer>();
    	posicionNueva.add(x);
    	posicionNueva.add(y);
    	return posicionNueva;
    }
    
    private Boolean buscarSiguienteLetra(String palabra, int nroLetra, ArrayList<Integer> posicionDeUltimaPalabra, 
    		Sentido sentidoOrigen, String cadenaFormada, ArrayList<Sentido> sentidosPermitidos) {
    	int siguienteNroLetra = nroLetra+1;
    	
    	if(palabra.length()==nroLetra) {
    		//No hay mas iteracciones que realizar
    		return palabra.equals(cadenaFormada);
    	}
    	
    	char letraABuscar = palabra.charAt(nroLetra);
    	//Buscar todos los posibles movimientos posibles
    	ArrayList<Movimiento> posiblesMovimientos = buscarMatchAdyacente(letraABuscar, posicionDeUltimaPalabra, sentidoOrigen);
    	//Ir a esos movimientos y comprobar de que se llegue al final de la cadena llamando de forma recursiva este método
    	//Hay que priorizar el sentido de origen siempre, en caso de fallar debe buscar otras laternativas
    	
    	if (!existenMovimientosPosibles(posiblesMovimientos)){
    		return false;
    	}
    	posiblesMovimientos = establecerPrioridadDeSentidoAnterior(posiblesMovimientos, sentidoOrigen);
    	cadenaFormada += letraABuscar;
    	int cant = posiblesMovimientos.size();
    	Boolean encontrado = false;
    	for(int i=0; i<cant;i++) {
    		Movimiento movimiento = posiblesMovimientos.get(i);
    		
    		/*
    		 * Este algortimo que incluye al SENTIDO de la dirección no tiene utilidad ahora mismo
    		 * dado que se pensó como que solo podía estar como máximo 2 sentidos unicamente,
    		 * 
    		 * EJ (ARRIBA-DERECHA) (DERECHA-ABAJO) (ABAJO-IZQUIERDA) (DERECHA-ARRIBA)
    		 * 
    		 * y así, sin embargo uno de los casos es el de CUCHILLAS que está en forma de U por lo tanto
    		 * hacía el recorrido en sentido (ABAJO-DERECHA-ARRIBA) para completarla.
    		 * 
    		 * Por eso mismo no tiene utilidad esto ahora mismo pero si se le quiere dar una restricción 
    		 * se la puede imponer la máxima cantidad de SENTIDOS PERMITIDOS con tan solo cambiar el 
    		 * VALOR MAXIMO PERMITIDO en uno de los métodos.
    		 */
    		Sentido sentidoDeMovimientoNuevo = movimiento.getSentidoRespectoOrigen();
    		sentidosPermitidos = agregarSentidoSiNoEstabaAntes(sentidosPermitidos, sentidoDeMovimientoNuevo);
    		if(esSentidoPermitido(sentidosPermitidos, sentidoDeMovimientoNuevo)) {
        		if(buscarSiguienteLetra(palabra, siguienteNroLetra, movimiento.getCoordenadas(), 
        				movimiento.getSentidoRespectoOrigen(), cadenaFormada, sentidosPermitidos)) {
        			encontrado=true;
        			break;
        		}
    		}
    	}
    	
    	return encontrado;
    }
    
    private ArrayList<Sentido> agregarSentidoSiNoEstabaAntes(ArrayList<Sentido> sentidosPermitidos, Sentido sentidoPostulante){
    	if(sePuedeAgregarMasSentidos(sentidosPermitidos) && 
    			!sentidosPermitidos.contains(sentidoPostulante)) {
    		sentidosPermitidos.add(sentidoPostulante);
    	}
    	return sentidosPermitidos;
    }
    
    private Boolean esSentidoPermitido(ArrayList<Sentido> permitidos, Sentido sentidoAIr) {
    	if(permitidos.contains(sentidoAIr)) {
    		return true;
    	}
    	//Se podria agegar siempre y cuando no haya ya 2 anteriores
    	
    	
    	
    	if(permitidos.size()<valorMaximoDeSentidosPermitidos) {
    		permitidos.add(sentidoAIr);
    		return true;
    	}
    	return false;
    }
    
    private ArrayList<Movimiento> establecerPrioridadDeSentidoAnterior(ArrayList<Movimiento> posiblesMovimientos, Sentido sentidoPrioritario){
    	ArrayList<Movimiento> movimientosOrdenados = new ArrayList<Movimiento>();
    	for(int i=0;i<posiblesMovimientos.size();i++) {
    		if(posiblesMovimientos.get(i).getSentidoRespectoOrigen().equals(sentidoPrioritario)) {
    			movimientosOrdenados.add(posiblesMovimientos.get(i));
    			posiblesMovimientos.remove(i);
    			break;
    		}
    	}
    	movimientosOrdenados.addAll(posiblesMovimientos);
    	return movimientosOrdenados;
    }
    
    private Boolean existenMovimientosPosibles(ArrayList<Movimiento> posiblesMovimientos) {
    	return posiblesMovimientos.size()>0;
    }
    
    private Boolean sePuedeAgregarMasSentidos(ArrayList<Sentido> sentidosPermitidos) {
    	return sentidosPermitidos.size()<2;
    }
    
    private ArrayList<Movimiento> buscarMatchAdyacente(char letraBuscada, ArrayList<Integer> posicionOrigen, Sentido sentidoOrigen) {
    	int x = posicionOrigen.get(0);
    	int y = posicionOrigen.get(1);
    	ArrayList<Integer> posAUX = new ArrayList<Integer>();
    	posAUX.add(x);
    	posAUX.add(y);
    	ArrayList<Movimiento> posiblesMovimientos = new ArrayList<Movimiento>();
    	if(posibleNuevaPosicion(posAUX, 0, 1) && existeLetraEnPosicion(letraBuscada, obtenerPosicion(posAUX, 0, 1)) && !sentidoOrigen.name().equals(Sentido.IZQUIERDA.name())) {
    		posiblesMovimientos.add(new Movimiento(obtenerPosicion(posAUX, 0, 1), Sentido.DERECHA));
    	}
    	//NO SE PORQUE posAUX se sobreescribe en algun momento
    	//posAUX= {posicionOrigen[0],posicionOrigen[1]};

    	if(posibleNuevaPosicion(posAUX, 0, -1) && existeLetraEnPosicion(letraBuscada, obtenerPosicion(posAUX, 0, -1)) && !sentidoOrigen.name().equals(Sentido.DERECHA.name())) {
    		posiblesMovimientos.add(new Movimiento(obtenerPosicion(posAUX, 0, -1), Sentido.IZQUIERDA));
    	}

    //	posAUX=posicionOrigen;
    	if(posibleNuevaPosicion(posAUX, -1, 0) && existeLetraEnPosicion(letraBuscada, obtenerPosicion(posAUX, -1, 0)) && !sentidoOrigen.name().equals(Sentido.ABAJO.name())) {
    		posiblesMovimientos.add(new Movimiento(obtenerPosicion(posAUX, -1, 0), Sentido.ARRIBA));
    	}

    //	posAUX=posicionOrigen;
    	if(posibleNuevaPosicion(posAUX, 1, 0) && existeLetraEnPosicion(letraBuscada, obtenerPosicion(posAUX, 1, 0)) && !sentidoOrigen.name().equals(Sentido.ARRIBA.name())) {
    		posiblesMovimientos.add(new Movimiento(obtenerPosicion(posAUX, 1, 0), Sentido.ABAJO));
    	}
    	return posiblesMovimientos;
    }
    
    private Boolean existeLetraEnPosicion(char letra, ArrayList<Integer> coordenadas) {
    	int x = coordenadas.get(0);
    	int y = coordenadas.get(1);
    	if(x<0 || y<0) {
    		return false;
    	}
    	char letraEncontrada = soup[x][y];
    	return letra == letraEncontrada;
    }
    
    public Boolean existePalabra(String palabra, ArrayList<Integer> ultimaPosicion) {
		
    	//Busco la letra fuente que es el primer caracter de la palabra
    	try {
    		ArrayList<Integer> coordenadasDeFuente = buscarPrimeraLetra(palabra.charAt(0), ultimaPosicion);
			
			buscarCadena(palabra, 1, coordenadasDeFuente, Sentido.INICIO);
	    	return true;
		} catch (ExceptionNoExisteSiguienteLetra e) {
			return false;
		}
    }
    
    private void buscarCadena(String cadena, int posicionCaracter, ArrayList<Integer> coordenadas, Sentido ultimoSentido) {
    	char siguienteLetraABuscar = cadena.charAt(posicionCaracter);
    	
    	
    	
    	buscarCadena(cadena, posicionCaracter+1, coordenadas, ultimoSentido);
    }
    
    
    private Boolean posibleNuevaPosicion(ArrayList<Integer> posicionDeUltimaPalabra, int sumX, int sumY) {
    	int tamanioMaxY = soup[0].length;
    	int tamanioMaxX = soup.length;
    	int x = posicionDeUltimaPalabra.get(0);
    	int y = posicionDeUltimaPalabra.get(1);
    	int posX = x+sumX;
    	int posY = y+sumY;
    	return posX >= 0 && posY >= 0 && posX<tamanioMaxX && posY < tamanioMaxY;
    }
    
    private ArrayList<Integer> obtenerPosicion(ArrayList<Integer> posicionDeUltimaPalabra, int sumX, int sumY) {
    	int x = posicionDeUltimaPalabra.get(0)+sumX;
    	int y = posicionDeUltimaPalabra.get(1)+sumY; 
    	ArrayList<Integer> pos = new ArrayList<Integer>();
    	pos.add(x);
    	pos.add(y);
    	return pos;
    }
    
    //Solo se dedica a buscar la posicion de la primera letra, es para luego intentar buscar en los Sentidos 
    //adyacentes la siguiente palabra
    private ArrayList<Integer> buscarPrimeraLetra(char letra, ArrayList<Integer> ultimaPosicionInicial) throws ExceptionNoExisteSiguienteLetra {
    	Integer tamanioMaxY = soup[0].length;
    	Integer tamanioMaxX = soup.length;
    	ArrayList<Integer> devolver =  new ArrayList<Integer>();
    	Integer posX = ultimaPosicionInicial.get(0);
    	Integer posY = ultimaPosicionInicial.get(1);
    	for(int i=posX; i<tamanioMaxX; i++) {
    		for(int j=posY; j<tamanioMaxY; j++) {
        		char letraEnSopa =soup[i][j];
    			if(letra==letraEnSopa) {
        			//MATCH!!!
    				devolver.add(i);
    				devolver.add(j);
        			return devolver;
        		}
        	}
    	}
    	throw new ExceptionNoExisteSiguienteLetra("No se puede formar la palabra a partir de esta fuente...");
    }
}
