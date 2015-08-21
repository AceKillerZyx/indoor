# 我很宅 AndroidApp项目结构说明
## gll 编写于 2015-04-30  15:50
##src包
包命名 com.indoorsy.frash.模块名.* (*目前主要包括activity,view,adapter,data,util)
common 公共组件，包括activity,view,adapter
http http模块
home home模块
myinfo 用户中心模块
homepage 首页模块
commodity 商品模块
shopping.cart 购物车模块
personal 个人模块
util 工具模块
## 资源文件
anim  放置动画
drawable 放置selector
drawable-*dpi 放置要适配的图片 图片文件命名规则 模块名_图片名_作用.后缀 如 indoorsy_welcome.png
selector文件命名规则 模块名_图片名_作用_selector.xml 如 common_button_selector.xml
对应的图片命名规则 模块名_图片名_作用_(unclick,press,click).后缀 如oq_home_commodity_tab_normal.png
layout 放置布局文件 布局命名规则 模块名_作用.xml 如 indoorsy_home.xml 表示home页的主界面布局
value 放置样式，颜色，string等 value资源命名规则 模块名_资源说明 如 indoorsy_homepage 表示homepage文字 common_bg 表示公共背景颜色
## 项目minSdkVersion为2.2

##打包密码kunmingmhw