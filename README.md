�û�ʹ���ֲ�<br>
���ȿ��������ú�����spy2servers.<br>
1. ����
���ص�ַ��<br>
�����Ƴ���http://www.blogjava.net/Files/xmatthew/spy2servers20080423.zip<br>
�������������,�������������2  Jetty��� �ŵ�libĿ¼�¡�<br>
api-docs <br>
Դ����<br>


���غ󣬰Ѷ��������ѹ�������ٰѵ���������ѹ�������ŵ�libĿ¼��
![](http://www.blogjava.net/images/blogjava_net/xmatthew/dir.JPG)

�û�ʹ���ֲᲿ��

����������spy2servers.xml�ļ� ��������Ҫ����һ��acitvemq��������һ��tomcat��������һ���ʼ�����֪ͨ�����һ����Ļ
������������

�����Ǿ���������ļ�(��ע��)

    <core-component> <!-- ���ú���������������Ҫ��  -->
        <simple-alertRule> <!-- ���� ��Ϣ��������-->
            <channel> <!-- �� ActiveMQJmxSpyComponent��TomcatJmxSpyComponent����������Ϣ������ת�� PrintScreenAlertComponent-->
                <from value="ActiveMQJmxSpyComponent"/>
                <from value="TomcatJmxSpyComponent"/>
                <to value="PrintScreenAlertComponent"/>
            </channel>
            <channel><!-- �� TomcatJmxSpyComponent����������Ϣ������ת�� EmailAlertComponent-->
                <from value="TomcatJmxSpyComponent"/>
                <to value="EmailAlertComponent" />
            </channel>
        </simple-alertRule>
    </core-component>
    
    <jmxService-component /> <!-- ����jmx��ط�����IPͨ�� java������������ Ĭ��Ϊ1616 -->

    <!-- ���� PrintScreenAlertComponent���-->
    <beans:bean class="org.xmatthew.spy2servers.component.alert.PrintScreenAlertComponent">
        <beans:property name="name" value="PrintScreenAlertComponent"></beans:property>
    </beans:bean>
    
    <!-- 
        host ��ص�Jmx����IP port��ص�Jmx����˿� detectInterval���ʱ���� ������ ���� Ĭ��ֵΪ5000
        queueSuspendNotifyTime ��Ϣ����û����Ϣ����ص�ʱ�򣬳������ʱ�俪ʼ����
        name ������ƣ��ᱻ<simple-alertRule>ʹ��
    -->
    <activeMQJmxSpy host="127.0.0.1" port="1099" queueSuspendNotifyTime="2000" name="ActiveMQJmxSpyComponent">
        <!-- ���ڴ� ��ط��� -->
        <!-- memoryUsedPercentToAlert �����ٽ�ֵ �ڴ�ʹ�õİٷֱ� ʹ�õ��ڴ�ֵ / ����ڴ�ֵ�� ע�����ֵ���ã� memoryUsedToAlert�Զ�ʧЧ-->
        <!-- alertAfterKeepTimeLive �����ٽ�ֵ���ֶ�ú󣬿�ʼ���� ��λ�롣 ע��ֵû��Ĭ��ֵ������������򲻻ᴥ���������� -->
        <heapMemorySpy memoryUsedPercentToAlert="90" alertAfterKeepTimeLive="10"/>
        <!-- �Ƕ��ڴ� ��ط��� ˵��ͬ��-->
        <noneHeapMemorySpy memoryUsedPercentToAlert="90" alertAfterKeepTimeLive="10"/>
        <!-- filesOpenedPercentToAlert �����ٽ�ֵ �ļ������ٷֱ� ��ʼ���ļ��� / �����ļ�����  ע�����ֵ���ã� filesOpenedToAlert�Զ�ʧЧ -->
        <!-- alertAfterKeepTimeLive �����ٽ�ֵ���ֶ�ú󣬿�ʼ���� ��λ�롣 ע��ֵû��Ĭ��ֵ������������򲻻ᴥ���������� -->
        <fileSpy filesOpenedPercentToAlert="90" alertAfterKeepTimeLive="10"/>
        <destinationNamesToWatch> <!-- ��ص���Ϣ���У����Ϊ�գ���ʾ��ص�ǰ�����ж��� -->
            <queue value="Test.Queue" />
            <queue value="aaa" />
        </destinationNamesToWatch>
        <llegalIps> <!-- ��ص����ӵĺϷ�IP�����Ϊ�գ���ʾ������� -->
            <ip value="127.0.0.1" />
            <ip value="192.168.0.1" />
        </llegalIps>
    </activeMQJmxSpy>

    <tomcatJmxSpy host="127.0.0.1" port="8060">
        <dataSourcesSpy> <!-- ����Դ��ط��� �������ö��-->
             <!-- numActivePercentToAlert �����ٽ�ֵ ���ӳ�������ռ�ñ��� ʹ�õ������� / ������������  ע�����ֵ���ã� numActiveToAlert�Զ�ʧЧ -->
             <!-- dataSourceName ��ص������������� -->
            <dataSourceSpy numActivePercentToAlert="90" dataSourceName="jdbc/opendb" />
        </dataSourcesSpy>
        <webModuleSpy> <!-- Web Module ��ط��� -->
             <!-- ��ص�web module�б����ģ��״̬Ϊstop��undeploy��ᱨ�������� -->
            <module value="/" />
            <module value="/jsp-examples" />
        </webModuleSpy>
    </tomcatJmxSpy>
    
    <emailAlert> 
        <emails> <!-- �����ʼ�֪ͨ�б� �������ö��-->
            <email value="ant_miracle@163.com" />
        </emails> <!-- �����ʼ�������������Ϣ -->
        <emailAccount server="smtp.163.com" serverPort="25" loginName="xxxx"
            loginPwd="xxxx" sender="ant_miracle@163.com" sendNick="EmailAlertComponent"/>
    </emailAlert>

  <jetty> <!-- �������÷�����  -->
    <connectors>
      <nioConnector port="7758" /> <!-- using nio connector port is 7758 -->
    </connectors>
    <handlers>
        <!-- �������û���web ��ʽ��ƽ̨������ servlet contextΪ /admin  -->
      <servlet servletClass="org.xmatthew.spy2servers.component.web.ComponentsViewServlet" path="/admin" /> 
    </handlers>
  </jetty>


������ɺ����� spy2servers/binĿ¼�������windowsƽ̨������ start.bat�����linuxƽ̨���� start.sh
��������ʾ���£�

<pre>
INFO  Main                           - Server starting
INFO  log                            - Logging to org.slf4j.impl.JCLLoggerAdapter(org.mortbay.log) via org.mortbay.log.Slf4jLog
INFO  CoreComponent                  - plug component CoreComponent
INFO  CoreComponent                  - plug component JmxServiceComponent
INFO  CoreComponent                  - plug component PrintScreenAlertComponent
INFO  CoreComponent                  - plug component ActiveMQJmxSpyComponent
INFO  CoreComponent                  - plug component TomcatJmxSpyComponent
INFO  CoreComponent                  - plug component EmailAlertComponent
INFO  log                            - jetty-6.1.4
INFO  log                            - Started SelectChannelConnector@0.0.0.0:7758
INFO  NetShutdownHandlerCommand      - Listening on port 8858
</pre>
<br>
��ʾ�Ѿ�spy2servers�Ѿ������ɹ�

���棬���ǿ�ͨ�� jmx��web���鿴�������
Jmx��ʾ���£�
![](http://www.blogjava.net/images/blogjava_net/xmatthew/JMX2.JPG)
web��ʾ����
![](http://www.blogjava.net/images/blogjava_net/xmatthew/spy2serves-web-start.JPG)
![](http://www.blogjava.net/images/blogjava_net/xmatthew/spy2serves-web-start.JPG)