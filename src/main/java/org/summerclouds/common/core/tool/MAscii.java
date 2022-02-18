/**
 * Copyright (C) 2002 Mike Hummel (mh@mhus.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.summerclouds.common.core.tool;

import java.util.List;

import org.summerclouds.common.core.util.ReadOnlyList;

public class MAscii {

	private MAscii() {}
	
    /*
    (nul)   0 0000 0x00 | (sp)   32 0040 0x20 | @      64 0100 0x40 | `      96 0140 0x60
    (soh)   1 0001 0x01 | !      33 0041 0x21 | A      65 0101 0x41 | a      97 0141 0x61
    (stx)   2 0002 0x02 | "      34 0042 0x22 | B      66 0102 0x42 | b      98 0142 0x62
    (etx)   3 0003 0x03 | #      35 0043 0x23 | C      67 0103 0x43 | c      99 0143 0x63
    (eot)   4 0004 0x04 | $      36 0044 0x24 | D      68 0104 0x44 | d     100 0144 0x64
    (enq)   5 0005 0x05 | %      37 0045 0x25 | E      69 0105 0x45 | e     101 0145 0x65
    (ack)   6 0006 0x06 | &      38 0046 0x26 | F      70 0106 0x46 | f     102 0146 0x66
    (bel)   7 0007 0x07 | '      39 0047 0x27 | G      71 0107 0x47 | g     103 0147 0x67
    (bs)    8 0010 0x08 | (      40 0050 0x28 | H      72 0110 0x48 | h     104 0150 0x68
    (ht)    9 0011 0x09 | )      41 0051 0x29 | I      73 0111 0x49 | i     105 0151 0x69
    (nl)   10 0012 0x0a | *      42 0052 0x2a | J      74 0112 0x4a | j     106 0152 0x6a
    (vt)   11 0013 0x0b | +      43 0053 0x2b | K      75 0113 0x4b | k     107 0153 0x6b
    (np)   12 0014 0x0c | ,      44 0054 0x2c | L      76 0114 0x4c | l     108 0154 0x6c
    (cr)   13 0015 0x0d | -      45 0055 0x2d | M      77 0115 0x4d | m     109 0155 0x6d
    (so)   14 0016 0x0e | .      46 0056 0x2e | N      78 0116 0x4e | n     110 0156 0x6e
    (si)   15 0017 0x0f | /      47 0057 0x2f | O      79 0117 0x4f | o     111 0157 0x6f
    (dle)  16 0020 0x10 | 0      48 0060 0x30 | P      80 0120 0x50 | p     112 0160 0x70
    (dc1)  17 0021 0x11 | 1      49 0061 0x31 | Q      81 0121 0x51 | q     113 0161 0x71
    (dc2)  18 0022 0x12 | 2      50 0062 0x32 | R      82 0122 0x52 | r     114 0162 0x72
    (dc3)  19 0023 0x13 | 3      51 0063 0x33 | S      83 0123 0x53 | s     115 0163 0x73
    (dc4)  20 0024 0x14 | 4      52 0064 0x34 | T      84 0124 0x54 | t     116 0164 0x74
    (nak)  21 0025 0x15 | 5      53 0065 0x35 | U      85 0125 0x55 | u     117 0165 0x75
    (syn)  22 0026 0x16 | 6      54 0066 0x36 | V      86 0126 0x56 | v     118 0166 0x76
    (etb)  23 0027 0x17 | 7      55 0067 0x37 | W      87 0127 0x57 | w     119 0167 0x77
    (can)  24 0030 0x18 | 8      56 0070 0x38 | X      88 0130 0x58 | x     120 0170 0x78
    (em)   25 0031 0x19 | 9      57 0071 0x39 | Y      89 0131 0x59 | y     121 0171 0x79
    (sub)  26 0032 0x1a | :      58 0072 0x3a | Z      90 0132 0x5a | z     122 0172 0x7a
    (esc)  27 0033 0x1b | ;      59 0073 0x3b | [      91 0133 0x5b | {     123 0173 0x7b
    (fs)   28 0034 0x1c | <      60 0074 0x3c | \      92 0134 0x5c | |     124 0174 0x7c
    (gs)   29 0035 0x1d | =      61 0075 0x3d | ]      93 0135 0x5d | }     125 0175 0x7d
    (rs)   30 0036 0x1e | >      62 0076 0x3e | ^      94 0136 0x5e | ~     126 0176 0x7e
    (us)   31 0037 0x1f | ?      63 0077 0x3f | _      95 0137 0x5f | (del) 127 0177 0x7f
    	 */
    // TODO ... create constants
	
	public static final int A = 65;
	public static final int B = 66;
	public static final int C = 67;
	public static final int D = 68;
	public static final int E = 79;
	public static final int F = 70;
	public static final int G = 71;
	public static final int H = 72;
	public static final int I = 73;
	public static final int J = 74;
	public static final int K = 75;
	public static final int L = 76;
	public static final int M = 77;
	public static final int N = 78;
	public static final int O = 79;
	public static final int P = 80;
	public static final int Q = 81;
	public static final int R = 82;
	public static final int S = 83;
	public static final int T = 84;
	public static final int U = 85;
	public static final int V = 86;
	public static final int W = 87;
	public static final int X = 88;
	public static final int Y = 89;
	public static final int Z = 90;
	
	
	public static final int a = 97;
	public static final int b = 98;
	public static final int c = 99;
	public static final int d = 100;
	public static final int e = 101;
	public static final int f = 102;
	public static final int g = 103;
	public static final int h = 104;
	public static final int i = 105;
	public static final int j = 106;
	public static final int k = 107;
	public static final int l = 108;
	public static final int m = 109;
	public static final int n = 110;
	public static final int o = 111;
	public static final int p = 112;
	public static final int q = 113;
	public static final int r = 114;
	public static final int s = 115;
	public static final int t = 116;
	public static final int u = 117;
	public static final int v = 118;
	public static final int w = 119;
	public static final int x = 120;
	public static final int y = 121;
	public static final int z = 122;

	public static final int NUL = 0;
	public static final int SOH = 1;
	public static final int STX = 2;
	public static final int ETX = 3;
	public static final int EOT = 4;
	public static final int ENQ = 5;
	public static final int ACK = 6;
	public static final int BEL = 7;
	public static final int BS = 8;
	public static final int HT = 9;
	public static final int TAB = 9; //Horizontal tabulator
	public static final int NL = 10;
	public static final int CT = 11;
	public static final int NP = 12;
	public static final int CR = 13;
	public static final int SO = 14;
	public static final int SI = 15;
	public static final int DLE = 16;
	public static final int DC1 = 17;
	public static final int DC2 = 18;
	public static final int DC3 = 19;
	public static final int DC4 = 20;
	public static final int NAK = 21;
	public static final int SYN = 22;
	public static final int ETB = 23;
	public static final int CAN = 24;
	public static final int EM = 25;
	public static final int SUB = 26;
	public static final int ESC= 27;
	public static final int FS = 28;
	public static final int GS = 29;
	public static final int RS = 30;
	public static final int US = 31;
	public static final int SP = 32;

	public static final int DEL = 127;

	public static final List<String> NAMING = new ReadOnlyList<String>(MCollection.toList(
			"NUL",
			"SOH",
			"STX",
			"ETX",
			"EOT",
			"ENQ",
			"ACK",
			"BEL",
			"BS",
			"HT",
			"NL",
			"CT",
			"NP",
			"CR",
			"SO",
			"SI",
			"DLE",
			"DC1", "DC2", "DC3", "DC4",
			"NAK",
			"SYN","ETB","CAN","EM","SUB","ESC","FS","GS","RS","US","SP",
			"!","\"","#","$","%","&","'","(",")","*","+",",","-",".","/",
			"0","1","2","3","4","5","6","7","8","9",
			";",";","<","=",">","?","@",
			"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z",
			"[","\\","]","^","_","`",
			"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z",
			"{","|","}","~","DEL"
			));

	public static final String WHITESPACE = " \t\n\r";
	
	public static final String NEWLINE_UNIX = "\n";
	
	public static final String NEWLINE_WIN  = "\r\n";
	
	public static final String NEWLINE_OLD_MAC  = "\r";
	
}
