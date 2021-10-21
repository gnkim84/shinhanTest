package com.shinvest.ap.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.ip.dsl.Tcp;
import org.springframework.integration.ip.dsl.TcpInboundGatewaySpec;
import org.springframework.integration.ip.tcp.connection.TcpNioServerConnectionFactory;

import com.shinvest.ap.config.props.EaiSocketProps;
import com.shinvest.ap.socket.EaiSocketDeserializer;
import com.shinvest.ap.socket.EaiSocketSerializer;
import com.shinvest.ap.socket.EaiSocketServerHandler;

@Configuration
@EnableIntegration
public class EaiSocketServerConfig {
	
	@Resource(name="eaiSocketProps")
	private EaiSocketProps props;

	@Resource(name="eaiSocketServerHandler")
	private EaiSocketServerHandler eaiSocketServerHandler;
	
	@Resource(name="eaiSocketSerializer")
	private EaiSocketSerializer eaiSocketSerializer;
	
	@Resource(name="eaiSocketDeserializer")
	private EaiSocketDeserializer eaiSocketDeserializer;
	
	@Bean
	public IntegrationFlow eaiSocketServer() {
		return IntegrationFlows.from(eaiSocketInboundGatewaySpec())
				.handle(eaiSocketServerHandler::handler)
				.get();
	}
	
	@Bean
	public TcpNioServerConnectionFactory eaiSocketServerConnectionFactory() {
		TcpNioServerConnectionFactory factory = new TcpNioServerConnectionFactory(props.getServerPort());
		//InputStream
		factory.setDeserializer(eaiSocketDeserializer);
		//OutputStream
		factory.setSerializer(eaiSocketSerializer);
		factory.setSingleUse(true);
		return factory;
	}
	
	@Bean
	public TcpInboundGatewaySpec eaiSocketInboundGatewaySpec() {
		return Tcp.inboundGateway(eaiSocketServerConnectionFactory());
	}
}
