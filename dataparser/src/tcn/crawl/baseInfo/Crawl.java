package crawl.baseInfo;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import com.bmtech.utils.bmfs.MDir;
import com.bmtech.utils.http.IteratorableCrawler;
import com.bmtech.utils.http.IteratorableCrawler.GenEntry;
import com.bmtech.utils.http.IteratorableCrawler.LineGenor;

public class Crawl{

	public static void main(String[] args) throws IOException {
		File f = new File("./config/stock/ids.txt");
		MDir mdir = new MDir(
				new File("/stock/base"));
		LineGenor gen = new LineGenor(f){
			String crt = null;
			URL base = new URL("http://f10.eastmoney.com/f10_v2/");
			String[]urls = new String[]{
					"OperationsRequired.aspx?type=soft&code=szREPLACE&timetip=635296569168597500#zxzb-0",
					"ShareholderResearch.aspx?type=soft&code=szREPLACE&timetip=635296569168597500#gdrs-0",
					"BusinessAnalysis.aspx?type=soft&code=szREPLACE&timetip=635296569168597500#zyfw-0",
					"CoreConception.aspx?type=soft&code=szREPLACE&timetip=635296569168597500#hxgn-0",
					"NewsBulletin.aspx?type=soft&code=szREPLACE&timetip=635296569168597500#gsxw-0",
					"CompanyBigNews.aspx?type=soft&code=szREPLACE&timetip=635296569168597500#dstx-0",
					"CompanySurvey.aspx?type=soft&code=szREPLACE&timetip=635296569168597500#jbzl-0",
					"IndustryAnalysis.aspx?type=soft&code=szREPLACE&timetip=635296569168597500#hyzx-0",
					"ProfitForecast.aspx?type=soft&code=szREPLACE&timetip=635296569168597500#pjtj-0",
					"ResearchReport.aspx?type=soft&code=szREPLACE&timetip=635296569168597500#ybzy-0",
					"FinanceAnalysis.aspx?type=soft&code=szREPLACE&timetip=635296569168597500#zyzb-0",
					"BonusFinancing.aspx?type=soft&code=szREPLACE&timetip=635296569168597500#fhyx-0",
					"CapitalStockStructure.aspx?type=soft&code=szREPLACE&timetip=635296569168597500#xsjj-0",
					"CompanyManagement.aspx?type=soft&code=szREPLACE&timetip=635296569168597500#gglb-0",
					"CapitalOperation.aspx?type=soft&code=szREPLACE&timetip=635296569168597500#mjzjly-0",
					"StockRelationship.aspx?type=soft&code=szREPLACE&timetip=635296569168597500#thygg-0",

			};
			int curs = -1;
			@Override
			public synchronized GenEntry next() throws IOException {
				curs ++;
				if(this.crt == null || curs == urls.length){
					crt = this.nextLine();
					curs = 0;
				}
				if(crt == null){
					return null;
				}
				SInfo si = new SInfo(crt);
				
				String url = urls[curs];
				return new GenEntry(
						new URL(base, url.replace("REPLACE", si.id)),
						si.id +"."+ url.substring(0, url.indexOf("."))
				);
				
			}

		};
		
		IteratorableCrawler.crawl(mdir, gen, 1, 1000);
	}
}
