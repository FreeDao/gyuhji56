package test;

import static java.util.Arrays.asList;

import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Locale;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;

public class testMongo {
	static void xx() {
		"".getClass().getMethods()[0].getReturnType();
	}

	static void insert(MongoDatabase db) throws ParseException {
		// Runtime.getRuntime().exec(command)
		new Comparator() {

			@Override
			public int compare(Object o1, Object o2) {
				// TODO Auto-generated method stub
				return 0;
			}

		};

		DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",
				Locale.ENGLISH);
		db.getCollection("restaurants")
				.insertOne(
						new Document("address", new Document()
								.append("street", "2 Avenue")
								.append("zipcode", "10075")
								.append("building", "1480")
								.append("coord",
										asList(-73.9557413, 40.7720266)))
								.append("borough", "Manhattan")
								.append("cuisine", "Italian")
								.append("grades",
										asList(new Document()
												.append("date",
														format.parse("2014-10-01T00:00:00Z"))
												.append("grade", "A")
												.append("score", 11),
												new Document()
														.append("date",
																format.parse("2014-01-16T00:00:00Z"))
														.append("grade", "B")
														.append("score", 17)))
								.append("name", "Vella")
								.append("restaurant_id", "41704620"));
	}

	public static void main(String[] args) throws UnknownHostException,
			MongoException, ParseException {

		MongoClient mongoClient = new MongoClient();
		try {
			MongoDatabase db = mongoClient.getDatabase("local");

			insert(db);
			find(db);
		} finally {
			mongoClient.close();
		}
		// Mongo mg = new Mongo();
		// // 查询所有的Database
		// for (String name : mg.getDatabaseNames()) {
		// System.out.println("dbName: " + name);
		// }
		//
		// DB db = mg.getDB("test");
		// // 查询所有的聚集集合
		// for (String name : db.getCollectionNames()) {
		// System.out.println("collectionName: " + name);
		// }
		//
		// DBCollection users = db.getCollection("users");
		//
		// // 查询所有的数据
		// DBCursor cur = users.find();
		// while (cur.hasNext()) {
		// System.out.println(cur.next());
		// }
		// System.out.println(cur.count());
		// System.out.println(cur.getCursorId());
		// System.out.println(JSON.serialize(cur));
	}

	private static void find(MongoDatabase db) {
		FindIterable<Document> iterable = db.getCollection("restaurants")
				.find();

		iterable.forEach(new Block<Document>() {
			@Override
			public void apply(final Document document) {
				System.out.println(document);
			}
		});

	}
}