#####  介绍   
   &nbsp;&nbsp;&nbsp;&nbsp;这是个定向预测基金涨跌的小程序工具,数据来源爬取的第三方网站(可以查看类com.eatrho.fundtool.Constant中url，免得说有推销之嫌)  
  
  
##### 相关技术  
jdk1.8  
fastjson  
jsoup  
httpclient  
  
##### 最后说明  
&nbsp;&nbsp;&nbsp;&nbsp;该小工具仅仅是根据股票型或者持股比重较大的基金进行计算预测，进行预测时，仅仅是在当时交易日，实时获取股票涨跌情况，及各个股票在所有预测的基金中所占比例的一个简单计算。（还有个前提是，基金本身没有进行调仓） 预测的结果为正数表示该基金会涨，反之，如果为负数表示该基金会跌，数值的大小不具有因果性，仅仅是有点相关性。由于证券交易时间为 09:30-11:30 , 13:30-15:00(以A股市场为例)，所以该程序比较理想的测试时间为14:30~15:00(个人认为，越接近15:00，预测会准确些)