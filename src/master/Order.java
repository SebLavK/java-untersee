package master;

import java.util.function.Consumer;

/**
*@author Sebas Lavigne
*/

public class Order {
	
	private Consumer<Object> verb;
	private Object object;
	private String verbose;
	
	public Order(Consumer<Object> verb, Object object, String verbose) {
		super();
		this.verb = verb;
		this.object = object;
		this.verbose = verbose;
	}

	/**
	 * @return the consumer
	 */
	public Consumer<Object> getVerb() {
		return verb;
	}

	/**
	 * @param verb the consumer to set
	 */
	public void setVerb(Consumer<Object> verb) {
		this.verb = verb;
	}

	/**
	 * @return the object
	 */
	public Object getObject() {
		return object;
	}

	/**
	 * @param object the object to set
	 */
	public void setObject(Object object) {
		this.object = object;
	}

	/**
	 * @return the verbose
	 */
	public String getVerbose() {
		return verbose;
	}

	/**
	 * @param verbose the verbose to set
	 */
	public void setVerbose(String verbose) {
		this.verbose = verbose;
	}

	
}
