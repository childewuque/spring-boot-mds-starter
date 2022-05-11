spring-boot-mds-starter 目的:在一个springboot项目中使用多个数据源，多数据源通过配置使用，无需在业务项目中编写大量代码支持关系型数据库访问。支持mybatis，jpa，nutz，hibernate，jdbctemplate等等多个持久层框架，并可使用shardingpshere-jdbc以支持分库分表。

功能特性：

1 支持mysql,支持oracle，并支持同一个项目同时使用mysql，oracle，其他关系型数据库并使用mybatis,jdbctemplate理论上支持；其他关系型数据库并使用jpa/hibernate不支持

2 支持任意多的动态数据源 更改数据源配置需要重启服务生效

3 支持多种持久层框架:mybatis,jdbc,jdbctemplate,jpa(hibernate5实现),hibernate5,nutz， 同一项目只能使用其中一种。mybatis plus等基于mybatis支持。

4 支持默认数据库定义 多数据库时可以简化使用

5 支持单数据源无感知使用 单数据源模式，类似单源配置，完全原生使用方法

6 支持JPA/hibernate动态包扫描，不同数据源的包定义不一样，通过配置完成JPA包动态扫描

7 支持在service或dao的类/方法上进行注解切换数据源 根据需要灵活使用

8 支持数据库多源负载均衡 读写均可负载均衡，尤其是可以把多个从库用于读，减轻单节点压力

9 支持读写分离--定义和使用独立的读写数据源

10 支持同一项目全部分库分表--pure模式 集成shardingsphere-jdbc，完全支持其功能

11 支持同一项目部分分库分表--hybrid模式 shardingsphere-jdbc数据源使用子集配置方式完成hybrid模式开发

12 支持spring单库的原生事务，完全使用spring事务机制

13 分库分表由shardingpshere-jdbc支持XA事务，仅支持单项目内使用，该组件目前不支持其他分布式复杂事务模式，比如跨应用事务，跨数据库类型等等

下面是经过验证的环境，其他环境请您自行测试验证:

mysql 5.6/5.7/8.*

oracle 11g

springboot 2.3.6-release

openjdk 11

HikariCP 3.4.5

shardingsphere-jdbc 5.0.0

说明文档: https://github.com/childewuque/spring-boot-mds-starter/wiki

逐步升级中，文档逐步完善，英文版说明待完善后再统一翻译，有问题，建议或疑问请提issue