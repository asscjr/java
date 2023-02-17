/*
Software para analisar dados de tênis, em uma determinada temporada. 
O arquivo de entrada tem o seguinte formato: data-início/duração/cidade/tipo/premiação/vencedor
O software é capaz das seguintes funções:
	. Qual é o jogador que tem mais pontos no ranking atual;
	. Qual é o jogador que recebeu mais em premiação;
	. Qual é o ranking de pontos atual dos jogadores;
	. Qual é o ranking de premiações atual dos jogadores;
	. Quantos torneios possuem datas conflitantes e;
	. A lista de torneios conflitantes entre si.
*/

//Bibliotecas utilizadas no código
import java.io.*;
import java.util.*;

//Classe main
public class main{
	//Método main
	public static void main(String[] args){
		//Código para capturar o nome do arquivo dado pelo usuário
		Scanner sc = new Scanner(System.in);
		System.out.println("Informe o nome do arquivo no formato: \"arquivo.txt\" ");
		String arq = sc.next();

		try{
			//Map para armazenar o nome dos vencedores de cada torneio como uma chave e a sua quantidade de vitórias e sua pontuação são armazenadas num ArrayList 
			Map<String, ArrayList<Integer>> vencedor = new HashMap<String, ArrayList<Integer>>();
			//Arraylist para armazenar os torneios
			ArrayList<String> torneios = new ArrayList<String>();
			//Código para ler o arquivo informado pelo usuário
			BufferedReader in = new BufferedReader(new FileReader(arq));
			String str;

			while((str = in.readLine()) != null){
				//Adicionamos um torneio a lista de torneios
				torneios.add(str);
				//Separamos a string encontrada para cada linha do arquivo para obtermos as informações de cada torneio
				String[] torneio = str.split("/");
				//Condição para caso o vencedor do torneio da linha corrente do arquivo já não tem vitórias em torneios anterioes ao corrente
				if (!(vencedor.containsKey(torneio[5]))){
					//Criamos a lista que conterá as informações do vencedor
					ArrayList<Integer> infoVencedor = new ArrayList<Integer>();
					//Código para obter a pontuação do vencedor e adicionamos a primeira posição da lista de informações
					if (torneio[3].equals("Grand Slam")){
						infoVencedor.add(2000);
					} else{
						int xxx = Integer.parseInt(torneio[3].substring(3,6));
						infoVencedor.add(xxx);
					}
					//Código para obter a premiação do vencedor e adicionamos a segunda posição da lista de informações
					String[] premiacao = torneio[4].split(" ");
					infoVencedor.add(Integer.parseInt(premiacao[0]));
					//Adicionamos a terceira posição da lista de informações o número de vitórias, como no caso é a primeira vez que o vencedor vence um torneio, o número 1 é adicionado
					infoVencedor.add(1);
					//Adicionamos a lista ao map
					vencedor.put(torneio[5], infoVencedor);
				  //Caso contrário esse vencedor corrente já ganhou torneios anteriores
				} else {
					//Criamos a lista que conterá as informações do vencedor
					ArrayList<Integer> infoVencedor = new ArrayList<Integer>();
					//Código para obter a pontuação do vencedor, somamos a pontuação anterior a nova e adicionamos a primeira posição da lista de informações
					if (torneio[3].equals("Grand Slam")){
						int pontuacao = vencedor.get(torneio[5]).get(0) + 2000;
						infoVencedor.add(pontuacao);
					} else{
						int xxx = Integer.parseInt(torneio[3].substring(3,6));
						int pontuacao = vencedor.get(torneio[5]).get(0) + xxx;
						infoVencedor.add(pontuacao);
					}
					//Código para obter a pontuação do vencedor, somamos a premiação anterior a nova e adicionamos a segunda posição da lista de informações
					String[] prem = torneio[4].split(" ");
					int premiacao = vencedor.get(torneio[5]).get(1) + Integer.parseInt(prem[0]);
					infoVencedor.add(premiacao);
					//Adicionamos a terceira posição da lista de informações o número de vitórias, somamos as vitórias anteriores a mais um nova e esse novo número é adicionado
					int vitorias = vencedor.get(torneio[5]).get(2) + 1;
					infoVencedor.add(vitorias);
					//Adicionamos a nova lista ao map
					vencedor.replace(torneio[5], infoVencedor);
				}
			}
			in.close();
			//Para criarmos o rank de pontuação e sabermos quem possui mais pontos utilizamos um vetor com o tamanho do map
			String[] rankPont = new String[vencedor.size()];
			int i = 0;
			//Passamos cada chave do map para esse novo vetor
			for (String chave : vencedor.keySet()) {
				rankPont[i] = chave;
				i++;
			}
			
			for (int j = 0; j < vencedor.size(); j++) {
				//Inicialmente, admitimos que o vencedor j possui o maior número de pontos
				int maior = vencedor.get(rankPont[j]).get(0);
				//Na posição j
				int pos = j;
				//Para cada sucessor de j, comparamos o número de pontos entre j e k
				for (int k = j + 1; k < vencedor.size(); k++) {
					//Caso o próximo tiver um núvero maior de pontos, o maior número de pontos e sua a posição são atualizados para k
					if (maior < vencedor.get(rankPont[k]).get(0)) {
						maior = vencedor.get(rankPont[k]).get(0);
						pos = k;
					}
					//Caso haja um empate no número de pontos
					else if (maior == vencedor.get(rankPont[k]).get(0)) {
						//Caso o próximo tiver um núvero maior de vitórias, o maior número de pontos e sua a posição são atualizados para k
						if (vencedor.get(rankPont[pos]).get(2) < vencedor.get(rankPont[k]).get(2)) {
							maior = vencedor.get(rankPont[k]).get(0);
							pos = k;
						}
						//Caso haja um empate no número de vitórias
						else if (vencedor.get(rankPont[pos]).get(2) == vencedor.get(rankPont[k]).get(2)) {
							//Armazenamos ambos os vencedores com pontos e vitórias iguais num ArrayList e utlizamos o método sort do mesmo para descobrir qual vem primeiro na ordem alfabética, o maior número de pontos e sua a posição são atualizados para k
							ArrayList<String> ordemAlfa = new ArrayList<String>();
							ordemAlfa.add(rankPont[k]);
							ordemAlfa.add(rankPont[pos]);
							Collections.sort(ordemAlfa);
							if (ordemAlfa.get(0).equals(rankPont[k])) {
								maior = vencedor.get(rankPont[k]).get(0);
								pos = k;
							}
						}
					}
				}
				//Função utilizada para ordenar esse vetor do vencedor com maior pontuação para o menor
				troca(j, pos, rankPont);
			}
			//Para sabermos quem possui a maior premiação também utilizaremos um vetor com tamanho do map
			String[] rankPrem = new String[vencedor.size()];
			i = 0;
			//Passamos cada chave do map para esse novo vetor
			for (String chave : vencedor.keySet()) {
				rankPrem[i] = chave;
				i++;
			}
			
			for (int j = 0; j < vencedor.size(); j++) {
				//Inicialmente, admitimos que o vencedor j possui a maior quantidade de premiações
				int maior = vencedor.get(rankPrem[j]).get(1);
				//Na posição j
				int pos = j;
				//Para cada sucessor de j, comparamos a quantidade de premiações entre j e k
				for (int k = j + 1; k < vencedor.size(); k++) {
					//Caso o próximo tiver uma quantidade de premiações maior, a maior quantidade de premiações e sua a posição são atualizados para k
					if (maior < vencedor.get(rankPrem[k]).get(1)) {
						maior = vencedor.get(rankPrem[k]).get(1);
						pos = k;
					}
				}
				//Função utilizada para ordenar esse vetor do vencedor com maior premiação para o menor
				troca(j, pos, rankPrem);
			}

			//Criamos um array list para armazenar os torneios com datas conflitantes entre si
			ArrayList<String> torneiosConflitantes = new ArrayList<String>();
			//Variável para saber a quantidade de torneios conflitantes
			int qntDC = 0;

			for (int j = 0; j < torneios.size(); j++) {
				//Pegamos a data do torneio j
				String data = torneios.get(j).substring(0,10);
				//Comparamos com seus sucessores para saber se são iguais
				for (int k = j + 1; k < torneios.size(); k++) {
					//Caso sejam iguais
					if (data.equals(torneios.get(k).substring(0,10))) {
						//Caso a data conflitante não tenha sido armazenada anteriormente nós podemos adicionar a data k e a data j a lista de torneios conflitantes e somarmos a variável de torneios conflitantes esses dois torneios conflitantes
						if (!(torneiosConflitantes.contains(torneios.get(j)))) {
							torneiosConflitantes.add(torneios.get(j));
							torneiosConflitantes.add(torneios.get(k));
							qntDC += 2;
						}
						//Caso a data conflitante j já foi armazenada a lista mas a data conflitante k não foi armazenada podemos armazenar a data k e somar esse nova data conflitante a variável de torneios conflitantes
						else if (!(torneiosConflitantes.contains(torneios.get(j)) && torneiosConflitantes.contains(torneios.get(k)))) {
							torneiosConflitantes.add(torneios.get(k));
							qntDC += 1;
						}
					}
				}
			}

			//Jogador com mais pontos no ranking atual
			System.out.println("Jogador com mais pontos no rank atual: " + rankPont[0] + " com " + vencedor.get(rankPont[0]).get(0) + " pontos.\n");
			
			//Jogador que recebeu mais em premiação
			System.out.println("Jogador com mais premiações: " + rankPrem[0] + " com " + vencedor.get(rankPrem[0]).get(1) + "$.\n");

			//Ranking de pontos atual dos jogadores
			System.out.println("===== Ranking =====\n");

			for (int j = 0; j < vencedor.size(); j++) {
				System.out.println((j+1) + " " + rankPont[j] + " / " + vencedor.get(rankPont[j]).get(0) + " pontos com " + vencedor.get(rankPont[j]).get(2) + " vitórias.");
			}
			
			System.out.println("\n");
			
			//Ranking de premiações atual dos jogadores
			System.out.println("===== Ranking Premiação =====\n");

			for (int j = 0; j < vencedor.size(); j++) {
				System.out.println((j+1) + " " + rankPrem[j] + " / " + vencedor.get(rankPrem[j]).get(1) + "$");
			}

			System.out.println("\n");

			//Todos os torneios
			System.out.println("===== Torneios =====\n");

			for (int j = 0; j < torneios.size(); j++) {
				System.out.println(torneios.get(j));
			}

			System.out.println("\n");
			
			//Quantidade de torneios conflitantes
			System.out.println("Quantidade de torneios conflitantes: " + qntDC + " torneios.\n");
			//Lista de torneios conflitantes
			System.out.println("===== Torneios Conflitantes =====\n");

			for (int j = 0; j < torneiosConflitantes.size(); j++) {
				System.out.println(torneiosConflitantes.get(j));
			}

			System.out.println("\n");

		}catch(IOException e) {
			System.out.println(e);
		}
	}
	//Função para trocar no vetor informado as suas posições
	static public void troca(int x, int y, String[] v){
		String temp = v[x];
		v[x] = v[y];
		v[y] = temp;
	}
}