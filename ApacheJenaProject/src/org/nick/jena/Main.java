package org.nick.jena;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.util.FileManager;



public class Main {

	private static String RDF_PATH = "C:\\Users\\NICK\\eclipse-workspace\\ApacheJenaProject\\src\\org\\nick\\jena\\data.rdf";
	
	// create a foaf a matic file here http://www.ldodds.com/foaf/foaf-a-matic.html
	// a model in jena is an object that is able to store in individuals described in an RDF file
	// the rdf file should not contain any spaces in the fields
	public static void main(String[] args) {
        sparQL();
	}
	
	
	private static void sparQL() {
		FileManager.get().addLocatorClassLoader(Main.class.getClassLoader());
        Model model = FileManager.get().loadModel("C:\\Users\\NICK\\eclipse-workspace\\ApacheJenaProject\\src\\org\\nick\\jena\\data.rdf");
        System.out.println("\n\n\n\n\n =================== TURTLE FORMAT ===================== \n\n\n");
        model.write(System.out, "TURTLE");
        System.out.println("\n\n\n\n\n =================== JSON FORMAT ===================== \n\n\n");
        model.write(System.out, "RDF/JSON");
        
        //the above query "SELECT everything where a person has name, the prefixes are shown in the first lines of the .rdf"
        //select everything where a person has name
        String queryString = 
        		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                "PREFIX foaf: <http://xmlns.com/foaf/0.1/> " +
        	    "SELECT * WHERE { " +
                " ?person foaf:name ?x ." +
        	    "} ";
        
        //select everything where a person has name = John
        String queryString2 = 
        		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                "PREFIX foaf: <http://xmlns.com/foaf/0.1/> " +
        	    "SELECT * WHERE { " +
                " ?person foaf:name ?x ." +
        	    " FILTER(?x = \"John\") " +
        	    "} ";
        		
        //select everything where a person knows another person
        String queryString3 = 
        		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                        "PREFIX foaf: <http://xmlns.com/foaf/0.1/> " +
                	    "SELECT * WHERE { " +
                        " ?person foaf:name ?x ." +
                	    " ?person foaf:knows ?person2 ." +
                	    "} ";
        
        
        //select everything where a person knows another person (person2) and this person2 should be named "John"
        String queryString4 =
        		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                        "PREFIX foaf: <http://xmlns.com/foaf/0.1/> " +
                	    "SELECT * WHERE { " +
                        " ?person foaf:name ?x ." +
                	    " ?person foaf:knows ?person2 ." +
                        " ?person2 foaf:name ?y ." + 
                	    " FILTER( ?y = \"Bob\") " +
                	    "} ";
        
        
        //select everything where a person has a nickname and this person knows another person whose name is one
        //of the "Bob" or "Eve"
        String queryString5 =
        		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                        "PREFIX foaf: <http://xmlns.com/foaf/0.1/> " +
                	    "SELECT * WHERE { " +
                        " ?person foaf:nick ?x ." +
                	    " ?person foaf:knows ?person2 ." +
                        " ?person2 foaf:name ?y ." + 
                	    " FILTER( ?y IN ( \"Bob\", \"Eve\")) " +
                	    "} ";
        
        
        sparQLExecute(queryString);
        sparQLExecute(queryString2);
        sparQLExecute(queryString3);
        sparQLExecute(queryString4);
        sparQLExecute(queryString5);
        
        
	}
	
	private static void sparQLExecute(String queryString) {
		FileManager.get().addLocatorClassLoader(Main.class.getClassLoader());
        Model model = FileManager.get().loadModel(RDF_PATH);
		Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        
        try {
        	
        	ResultSet results = qexec.execSelect(); //get the results and iterate through them
        	while(results.hasNext()) {
        		QuerySolution soln = results.nextSolution();
        		Literal name = soln.getLiteral("x"); //literal is a variable that we included in the query (shown in the query)
        		System.out.println(name);
        	}
        	System.out.println();
        }
        finally{
        	qexec.close();
        }
	}

}
