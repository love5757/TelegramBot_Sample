package kr.yerina.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * The type Properties util.
 */
public class PropertiesUtil extends PropertyPlaceholderConfigurer {
    /**
     * The Properties map.
     */
    private static Map<String, String> propertiesMap;
    /**
     * The Spring system properties mode.
     */
// Default as in PropertyPlaceholderConfigurer
    private int springSystemPropertiesMode = SYSTEM_PROPERTIES_MODE_FALLBACK;
    /**
     * The constant PARAM.
     */
    private static String PARAM = "param";

    /**
     * Sets system properties mode.
     *
     * @param systemPropertiesMode the system properties mode
     */
    @Override
    public void setSystemPropertiesMode(int systemPropertiesMode) {
        super.setSystemPropertiesMode(systemPropertiesMode);
        springSystemPropertiesMode = systemPropertiesMode;
    }

    /**
     * Process properties.
     *
     * @param beanFactory the bean factory
     * @param props       the props
     *
     * @throws BeansException the beans exception
     */
    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props) throws BeansException {
        super.processProperties(beanFactory, props);

        propertiesMap = new HashMap<String, String>();
        for (Object key : props.keySet()) {
            String keyStr = key.toString();
            String valueStr = resolvePlaceholder(keyStr, props, springSystemPropertiesMode);
            propertiesMap.put(keyStr, valueStr);
        }
    }

    /**
     * Gets property.
     *
     * @param name the name
     *
     * @return the property
     */
    public static String getProperty(String name) {
        if(propertiesMap.get(name) != null) return propertiesMap.get(name).toString();
        else return null;
    }

    /**
     * Gets property.
     *
     * @param name  the name
     * @param param the param
     *
     * @return the property
     */
    public static String getProperty(String name, String param[]) {
        
    	String value = propertiesMap.get(name).toString();
    	
    	for(int i=0;i<param.length;i++){
    		value = value.replaceAll(PARAM + i, param[i]);
    	}
    	
    	return value;
    
    }
    
}