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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author Waldo Urribarri - www.waldou.com
 *
 * This class represents the arguments of a search operation
 * on the server.
 *
 */
public class SearchArguments implements SearchConstants {

    private static final String OPER_SEPARATOR = " ~~~ ";
    private static final String BASE = "marc";
    private static final String CIPAR = "marc.par";
    private static final String PROLOGO = "S";
    private static final String OPCION = "avanzadaunabase";
    private static final String CAMPF = "22";
    private static final String OPER = "and";
    private static final String OPERADORES = OPER + OPER_SEPARATOR + OPER;
    private static final String CAMP = "7";
    private static final String TAG3048 = "";
    private static final String TAG4999 = "1";
    private static final int TO = 50;

    private String formato;
    private String campo;
    private String expresion;
    private String expre;
    private int to;
    private int from;

    private String encoding;
    private String queryString;
    private String postParams;

    public SearchArguments() {
        formato = FORMATO_LISTA;
        campo = "Todos";
        encoding = "UTF-8";
        to = TO;
    }

    public SearchArguments(String expresion, String expre) {
        this();
        this.expresion = expresion;
        this.expre = expre;
    }

    public SearchArguments(String campo, String expresion, String expre) {
        this(expresion, expre);
        this.campo = campo;
    }

    public SearchArguments(String campo, String expresion, String expre, String formato) {
        this(campo, expresion, expre);
        this.formato = formato;
    }

    public String getFormato() { return formato; }
    public void setFormato(String formato) {
        this.formato = formato;
        queryString = null;
        postParams = null;
    }
    public String getCampo() { return campo; }
    public void setCampo(String campo) {
        this.campo = campo;
        queryString = null;
        postParams = null;
    }
    public String getExpresion() { return expresion; }
    public void setExpresion(String expresion) {
        this.expresion = expresion;
        queryString = null;
        postParams = null;
    }
    public String getExpre() { return expre; }
    public void setExpre(String expre) {
        this.expre = expre;
        queryString = null;
        postParams = null;
    }
    public void setEncoding(String encoding) { this.encoding = encoding; }
    public int getTo() { return this.to; }
    public void setTo(int to) {
        this.to = to;
        queryString = null;
        postParams = null;
    }
    public int getFrom() { return this.from; }
    public void setFrom(int from) {
        this.from = from;
        queryString = null;
        postParams = null;
    }

    /**
     * This method uses all the arguments to contruct a query string
     * that will be used in the request to the server.
     * @return URL formatted query string.
     * @throws UnsupportedEncodingException
     */
    public String toQueryString() throws UnsupportedEncodingException {
        if(queryString == null) {
            final String EQUAL = "=";
            final String AND = "&";
            StringBuilder sb = new StringBuilder();
            sb.append(ARG_BASE).append(EQUAL).append(URLEncoder.encode(BASE, encoding)).append(AND)
                    .append(ARG_CIPAR).append(EQUAL).append(URLEncoder.encode(CIPAR, encoding)).append(AND)
                    .append(ARG_PROLOGO).append(EQUAL).append(URLEncoder.encode(PROLOGO, encoding)).append(AND)
                    .append(ARG_OPCION).append(EQUAL).append(URLEncoder.encode(OPCION, encoding)).append(AND)
                    .append(ARG_CAMPOS)
                        .append(EQUAL)
                            .append(URLEncoder.encode(CAMPOS.get(campo) + OPER_SEPARATOR + CAMPF, encoding))
                        .append(AND)
                    .append(ARG_CAMPF).append(EQUAL).append(URLEncoder.encode(CAMPF, encoding)).append(AND)
                    .append(ARG_OPERADORES).append(EQUAL).append(URLEncoder.encode(OPERADORES, encoding)).append(AND)
                    .append(ARG_EXPRESION)
                        .append(EQUAL)
                            .append(URLEncoder.encode(expresion + OPER_SEPARATOR + expre, encoding))
                        .append(AND)
                    .append(ARG_CAMP).append(EQUAL).append(URLEncoder.encode(CAMP, encoding)).append(AND)
                    .append(ARG_EXPRE).append(EQUAL).append(URLEncoder.encode(expre, encoding)).append(AND)
                    .append(ARG_OPER).append(EQUAL).append(URLEncoder.encode(OPER, encoding)).append(AND)
                    .append(ARG_FORMATO).append(EQUAL).append(URLEncoder.encode(formato, encoding));
            queryString = sb.toString();
        }
        return queryString;
    }

    /**
     * This method uses all the arguments to contruct a query string
     * that will be used in the request to the server.
     * @return URL formatted POST parameters.
     * @throws UnsupportedEncodingException
     */
    public String toPostParams() throws UnsupportedEncodingException {
        if(postParams == null) {
            final String EQUAL = "=";
            final String AND = "&";
            StringBuilder sb = new StringBuilder();
            sb.append(ARG_OPCION).append(EQUAL).append(URLEncoder.encode(OPCION, encoding)).append(AND)
                    .append(ARG_BASE).append(EQUAL).append(URLEncoder.encode(BASE, encoding)).append(AND)
                    .append(ARG_CIPAR).append(EQUAL).append(URLEncoder.encode(CIPAR, encoding)).append(AND)
                    .append(ARG_PROLOGO).append(EQUAL).append(URLEncoder.encode(PROLOGO, encoding)).append(AND)
                    .append(ARG_EXPRESION)
                        .append(EQUAL)
                            .append(URLEncoder.encode(expresion + OPER_SEPARATOR + expre, encoding))
                        .append(AND)
                    .append(ARG_CAMPOS)
                        .append(EQUAL)
                            .append(URLEncoder.encode(CAMPOS.get(campo) + OPER_SEPARATOR + CAMPF, encoding))
                        .append(AND)
                    .append(ARG_OPERADORES).append(EQUAL).append(URLEncoder.encode(OPERADORES, encoding)).append(AND)
                    .append(ARG_OPCION).append(EQUAL).append(URLEncoder.encode(OPCION, encoding)).append(AND)
                    .append(ARG_TAG3048).append(EQUAL).append(TAG3048).append(AND)
                    .append(ARG_TAG4999).append(EQUAL).append(TAG4999).append(AND)
                    .append(ARG_FROM).append(EQUAL).append(from).append(AND)
                    .append(ARG_TO).append(EQUAL).append(to).append(AND)
                    .append(ARG_FORMATO).append(EQUAL).append(URLEncoder.encode(formato, encoding));
            postParams = sb.toString();
        }
        return postParams;
    }

}
