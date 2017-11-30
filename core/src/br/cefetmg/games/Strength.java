/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games;

/**
 *
 * @author user
 */
public enum Strength {
    // Enum dedicado para o retorno visual da Vida dos Inimigos e da Forca da Torre
    // Sendo:
    // Quanto mais forte ou com mais vida -> mais próximo do violeta
    // Quanto mais fraco ou com menos vida -> mais próximo do vermelho
    // NOME                       R     G     B            PERCENTUAL
    VERMELHO,              //    255 ,  0  ,  0            1    , 0    , 0
    LARANJA,               //    255 , 165 ,  0            1    , 0.64 , 0
    AMARELO,               //    255 , 255 ,  0            1    , 1    , 0
    VERDE,                 //     0  , 255 ,  0            1    , 1    , 0
    CIANO,                 //     0  , 255 , 255           0    , 1    , 1
    AZUL,                  //     0  ,  0  , 255           0    , 0    , 1
    VIOLETA;               //     79 ,  47 ,  79           0.30 , 0.18 , 0.30
    
    
}
