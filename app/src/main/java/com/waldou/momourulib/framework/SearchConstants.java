/*******************************************************************************
 * Copyright 2017 Waldo Urribarri.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.waldou.momourulib.framework;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Waldo Urribarri - www.waldou.com
 *
 *
 */
public interface SearchConstants {

    // Argument names
    public static final String ARG_BASE = "base";
    public static final String ARG_CIPAR = "cipar";
    public static final String ARG_PROLOGO = "prologo";
    public static final String ARG_OPCION = "Opcion";
    public static final String ARG_CAMPOS = "Campos";
    public static final String ARG_CAMPF = "Campf";
    public static final String ARG_OPERADORES = "Operadores";
    public static final String ARG_EXPRESION = "Expresion";
    public static final String ARG_CAMP= "camp";
    public static final String ARG_EXPRE = "expre";
    public static final String ARG_OPER = "oper";
    public static final String ARG_FORMATO = "Formato";
    public static final String ARG_TAG3048 = "tag3048";
    public static final String ARG_TAG4999 = "tag4999";
    public static final String ARG_FROM = "from";
    public static final String ARG_TO = "to";

    // Book data output formats
    public static final String FORMATO_DETALLADO = "a";
    public static final String FORMATO_BREVE = "b";
    public static final String FORMATO_LISTA = "l";
    public static final String FORMATO_MARC = "m";

    public static Map<String, String> CAMPOS = new HashMap<String, String>() {
        {
            put("Todos", "---");
            put("Autor personal", "1");
            put("Autor institucional", "2");
            put("Nombre de Reunión", "3");
            put("Título", "4");
            put("Título de serie", "5");
            put("Materias (Temas)", "6");
            put("Editor/editorial", "7");
            put("tabla de contenido", "8");
            put("Fecha", "9");
            put("Caterogía Geográfica", "10");
            put("Tipo de tesis/trabajo", "11");
            put("Tipo de material", "22");
            put("Grado académico", "13");
            put("Facultad", "14");
            put("Excuela", "15");
            put("Tutor", "16");
            put("Lugar de publicación", "17");
            put("Profesor", "812");
            put("Cátedra", "813");

        };
    };

}
