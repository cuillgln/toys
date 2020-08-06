
package io.cuillgln.toys.infrastructure.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MQTTMain {

	public static void main(String[] args) {
		String topic = "realdata";
		String content = "Message from MqttPublishSample";
		int qos = 2;
		String broker = "tcp://106.14.169.36:1883";
		// String broker = "tcp://172.20.182.231:1883";
		String clientId = "consumer-cuillgln";

		MemoryPersistence persistence = new MemoryPersistence();
		try {
			MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setUserName("sdy");
			connOpts.setPassword("sdgl".toCharArray());
			connOpts.setCleanSession(true);
			System.out.println("Connecting to broker: " + broker);
			sampleClient.connect(connOpts);
			System.out.println("Connected to broker: " + broker);
			sampleClient.subscribe("realdata/#", new IMqttMessageListener() {

				@Override
				public void messageArrived(String topic, MqttMessage message) throws Exception {
					System.out.println("=================Received Message===============");
					System.out.println(topic);
					System.out.println(new String(message.getPayload()));
				}
			});
		} catch (MqttException me) {
			System.out.println("reason " + me.getReasonCode());
			System.out.println("msg " + me.getMessage());
			System.out.println("loc " + me.getLocalizedMessage());
			System.out.println("cause " + me.getCause());
			System.out.println("excep " + me);
			me.printStackTrace();
		}
	}
}
