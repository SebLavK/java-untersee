package master;

import java.util.function.Consumer;

/**
*@author Sebas Lavigne
*/

public class Order {
	
	private Consumer<Object> consumer;
	private Object object;
	private String verbose;
	
	public Order(Consumer<Object> consumer, Object object, String verbose) {
		super();
		this.consumer = consumer;
		this.object = object;
		this.verbose = verbose;
	}

	/**
	 * @return the consumer
	 */
	public Consumer<Object> getConsumer() {
		return consumer;
	}

	/**
	 * @param consumer the consumer to set
	 */
	public void setConsumer(Consumer<Object> consumer) {
		this.consumer = consumer;
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
