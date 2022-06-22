import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Program {

	public static void main(String[] args) {	
		try {
			File fXmlFile = new File("dados.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			List<String> list = new ArrayList<>();
			String allString = "";
			NodeList nList = doc.getElementsByTagName("row");
			//Procurando todos os elementos contidos no arquivo e armazenando eles em uma array
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					//LISTA todos os dias com o valor do faturamento em uma array separando eles por uma ,
					allString = eElement.getElementsByTagName("dia").item(0).getTextContent() + "," + eElement.getElementsByTagName("valor").item(0).getTextContent();
					list.add(allString);
				}
				
			}
			
			List<String> completList = list.stream().collect(Collectors.toList());
			Double lowInvoice = 0.00; //armazena o faturamento mais baixo
			Integer lowDay = 0; //armazena o dia do faturamento mais baixo
			Double highInvoice = 0.00; //armazena o faturamento mais alto
			Integer highDay = 0; //armazena o dia do faturamento mais alto
			Integer totalDays = 0; //armazena o total de dias em que o faturamento foi maior que 0
			Double sum = 0.00;
			for (String str: completList) {
				String[] allDays = str.split(",");
				int day = Integer.parseInt(allDays[0]);
				Double invoice = Double.parseDouble(allDays[1]);
				//Procurando o menor valor de faturamento ocorrido em um dia do mês e armazenando eles em uma variavel
				if (invoice < lowInvoice && invoice > 0 || lowInvoice == 0.00){
					lowInvoice = invoice;
					lowDay = day;		
				}
				//Procurando o maior valor de faturamento ocorrido em um dia do mês e armazenando eles em uma variavel
				if (invoice > highInvoice && invoice > 0){
					highInvoice = invoice;
					highDay = day;		
				}
				//Procurando todos registros com valor de faturamento maior que 0 e armazenando elas em uma variavel
				if (invoice > 0){
					totalDays = totalDays + 1;
					sum = sum + invoice;		
				}
			}
			//dividindo a quantidade de faturamento com os numeros de dias em que o faturamento era maior que 0 para criar a media de faturamento
			Double averageInvoice = sum / totalDays;
			Integer totalAverageDays = 0;
			//Procurando os dias com faturamento maior do que a media de faturamento do mes
			for (String str: completList) {
				String[] allDays = str.split(",");
				Double invoice = Double.parseDouble(allDays[1]);
				if (invoice >= averageInvoice) {
					totalAverageDays = totalAverageDays + 1;
					
				}
				
			}
			
			System.out.println("O Menor valor de faturamento foi no dia " + lowDay + " com faturamento de $" + String.format("%.2f", lowInvoice));
			System.out.println("O Maior valor de faturamento foi no dia " + highDay + " com faturamento de $" + String.format("%.2f", highInvoice));
			System.out.println("Número de dias no mês em que o valor de faturamento diário foi superior à média mensal ($" + String.format("%.2f", averageInvoice) + ") é de " + totalAverageDays + " dias.");	
			
		}
		catch (SAXException e) {
			System.out.println("Error: " + e.getMessage());
		}
		catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
		}
		catch (ParserConfigurationException e) {
			System.out.println("Error: " + e.getMessage());
		}
	
	}
}