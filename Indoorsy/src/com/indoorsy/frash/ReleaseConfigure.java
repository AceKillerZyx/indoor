package com.indoorsy.frash;

/**
 * @author gll
 * URL 类
 */
public class ReleaseConfigure {
	
	//线下测试
	//public static final String BASICURL = "http://192.168.0.118:8080/indoorsy/";
	
	//public static final String ROOT_IMAGE = "http://101.200.183.79:8080/indoorsy/" + "images/";//图片文件
	
	//服务器
	public static final String BASICURL = "http://101.200.183.79:8080/indoorsy/";
	
	public static final String ROOT_IMAGE = BASICURL + "images/";//图片文件
	
	public static final String COMMON_UPDATE_IMAGE = BASICURL + "androidimagesel";//图片上传

	public static final String COMMON_APK_UPDATE= BASICURL + "versionsel";//更新APK
	
	
	//-------------------------首页模块-----------------------
	//首页搜索栏
	public static final String HOMEPAGE_VALUE_TYPE_SEARCH = BASICURL + "goodskeysel.action";
	
	//首页商品类别
	public static final String HOMEPAGE_VALUE_TYPE_TYPE = BASICURL + "typesel";
	
	//首页轮播大图
	public static final String HOMEPAGE_VALUE_TYPE_VIEWPAGER = BASICURL + "adversel";
	
	//首页特价产品
	public static final String HOMEPAGE_VALUE_TYPE_GOODS = BASICURL + "bargaingoodssel";
	
	//产品详情信息
	public static final String HOMEPAGE_VALUE_TYPE_PRODUCT_DETAIL = BASICURL + "gooddeail.action";
	
	//图文详情
	public static final String HOMEPAGE_VALUE_TYPE_IMG_DETAIL = BASICURL + "coddeta.action";
	
	//商品添加收藏
	public static final String HOMEPAGE_VALUE_TYPE_ADD_COLLECTION = BASICURL + "collectgoodssave.action";
	
	//商品取消收藏
	public static final String HOMEPAGE_VALUE_TYPE_NO_COLLECTION = BASICURL + "collectgoodsdel.action";
	
	//加入购物车
	public static final String HOMEPAGE_VALUE_TYPE_ADD_SHOPCAR = BASICURL + "cartsave.action";
	
	//确认订单 - 查询积分
	public static final String HOMEPAGE_VALUE_TYPE_POINT = BASICURL + "integrasel.action";
	
	//确认订单 - 查询运费
	public static final String HOMEPAGE_VALUE_TYPE_FREIGHT = BASICURL + "freightsel.action";
	
	//订单详情
	public static final String ORDER_DETAILS =BASICURL+ "ordertrading.action";
	
	//首页商品类别点击进入到商品列表的二级类别
	public static final String HOMEPAGE_VALUE_PRODUCT_LIST_TYPE = BASICURL + "categorysel.action";
	
	//首页商品类别点击进入到商品列表全部类别
    public static final String HOMEPAGE_VALUE_PRODUCT_LIST_TYPE_ALL = BASICURL + "categorysel2";
    
    //首页商品类别点击 最新上线 进入到商品列表类别
    public static final String HOMEPAGE_VALUE_PRODUCT_LIST_TYPE_NEW = BASICURL + "goodssel";
    //首页广告点击 进入到商品列表
    public static final String HOMEPAGE_VALUE_PRODUCT_LIST_HOMEPAGE_AD = BASICURL + "advdetailesel.action";
    
    //单个订单上传
    public static final String HOMEPAGE_VALUE_UPLOAD_ADD_ORDER = BASICURL + "addorder.action";
    
    //多个订单上传
    public static final String HOMEPAGE_VALUE_UPLOAD_MORE_ADD_ORDER = BASICURL + "listorder.action";
    
    //订单上传-修改
	public static final String HOMEPAGE_VALUE_TYPE_UPDATE_ORDER = BASICURL + "updorder.action";
	
	//支付方式
	public static final String HOMEPAGE_VALUE_TYPE_PAY_ORDER_CREATEPAY = BASICURL + "createpay.action"; //支付宝
	public static final String HOMEPAGE_VALUE_TYPE_PAY_ORDER_WEIXINPAY = BASICURL + "weixinpay.action"; //微信
	public static final String HOMEPAGE_VALUE_TYPE_PAY_ORDER_UNIONPAYPAY = BASICURL + "unionpaypay.action"; //银联
	
    //支付成功修改订单状态
	public static final String HOMEPAGE_VALUE_TYPE_PAY_SUCCESS_STATE = BASICURL + "orderupastate2.action"; //修改订单状态
	
	
    //一级分类商品
    public static final String HOMEPAGE_VALUE_ONW_GOODS = BASICURL + "goodslb.action";
    
    //二级分类商品
    public static final String HOMEPAGE_VALUE_TWO_GOODS = BASICURL + "goodsorder2.action";
    //高鑫给的
   // public static final String HOMEPAGE_VALUE_TWO_GOODS = BASICURL + "goodsorder.action";
   
    
    
    //已秒杀
    public static final String HOMEPAGE_VALUE_PRODUCT_LIST_TYPE_LEFT_SECKKILL = BASICURL + "";
    
    //进行中的秒杀
    public static final String HOMEPAGE_VALUE_PRODUCT_LIST_TYPE_CURRENT_SECKKILL = BASICURL + "secksel";
    
    //即将开场
    public static final String HOMEPAGE_VALUE_PRODUCT_LIST_TYPE_RIGHT_SECKKILL = BASICURL + "";
    
    //食谱分享
    public static final String HOMEPAGE_VALUE_TYPE_RECIPES = BASICURL + "recipesel";
    
    //食谱详细内容
    public static final String HOMEPAGE_VALUE_TYPE_RECIPES_DETAIL = BASICURL + "recipeselect.action";

    //食谱详细-评论
    public static final String HOMEPAGE_VALUE_TYPE_RECIPES_DETAIL_COMMENT = BASICURL + "commentsave.action";
    
    //食谱详细-收藏食谱
    public static final String HOMEPAGE_VALUE_TYPE_RECIPES_DETAIL_COLLECTION = BASICURL + "savecollectrecipe.action";
    //食谱详细-取消 收藏食谱
    public static final String HOMEPAGE_VALUE_TYPE_RECIPES_DETAIL_NOCOLLECTION = BASICURL + "deletecollrecipe.action";
    
    //我的收藏
    public static final String HOMEPAGE_VALUE_TYPE_COLLECT_RECIPE = BASICURL + "selcollectrecipe.action"; //食谱
    public static final String HOMEPAGE_VALUE_TYPE_COLLECT_GOOD = BASICURL + "collectgoodssel.action";  //商品
    
    //我的订单
    public static final String HOMEPAGE_VALUE_TYPE_ORDER_ALL = BASICURL + "selordersum.action";  //查询全部订单
    public static final String HOMEPAGE_VALUE_TYPE_ORDER_STATE = BASICURL + "selectorder.action";  //根据不同状态查询订单
    public static final String HOMEPAGE_VALUE_TYPE_ORDER_REFUND = BASICURL + "orefund.action";  //查询退款记录
    public static final String HOMEPAGE_VALUE_TYPE_ORDER_CANCEL = BASICURL + "orderdel.action";  //取消订单
    public static final String HOMEPAGE_VALUE_TYPE_ORDER_CONFIRM = BASICURL + "orderupastate.action";  //确认收货订单
    
    //-----------------------购物车模块的--------------------
    //查询购物车
    public static final String SHOPPING_VALUE_TYPE_SEARCH = BASICURL + "selcart.action";
    
    //删除购物车
    public static final String SHOPPING_VALUE_TYPE_DELETE = BASICURL + "delcart.action";
    
	
	//-----------------------个人模块的--------------------
	//注册
	public static final String MYINFO_VALUE_TYPE_REGISTER = BASICURL + "uregist.action";
	//发送验证码
	public static final String MYINFO_VALUE_TYPE_SEND_CODE = BASICURL + "code.action";
	//登录
	public static final String MYINFO_VALUE_TYPE_LOGIN = BASICURL + "ulogin.action";
	//第三方登录
	public static final String MYINFO_VALUE_TYPE_THIRD_LOGIN = BASICURL + "thirdregister.action";
	//修改密码
	public static final String MYINFO_VALUE_TYPE_UPDATE = BASICURL+ "loginupdpwd.action";
	//注销登录
	public static final String MYINFO_VALUE_TYPE_LOGINOUT = BASICURL+ "udel.action";
	//第三方注销登录
	public static final String MYINFO_VALUE_TYPE_THIRD_LOGINOUT = BASICURL + "tlcancel.action";
	//找回密码
	public static final String MYINFO_VALUE_TYPE_RETRIEVE = BASICURL+ "updupwd.action";
	//查询个人资料
	public static final String PERSONAL_VALUE_TYPE_SEARCHUSERINFO = BASICURL +"usersel.action";
	//修改个人资料
	public static final String PERSONAL_VALUE_TYPE_UPDATEUSERINFO = BASICURL +"userupd.action";
	
	//查询收货地址
	public static final String PERSONAL_VALUE_TYPE_SEARCH_ADDRESS = BASICURL +"addersssel.action";
	
	//添加收货地址
	public static final String PERSONAL_VALUE_TYPE_NEWADDRESS = BASICURL +"addersssave.action";
	
	//修改收货地址
	public static final String PERSONAL_VALUE_TYPE_UPDATEADDRESS = BASICURL +"adderssupd.action";
	
	//删除收货地址
	public static final String PERSONAL_VALUE_TYPE_DELETEADDRESS = BASICURL +"adderssdel.action";
	
	
	//查询积分记录
	public static final String PERSONAL_VALUE_TYPE_INTEGRALRECORD = BASICURL +"integralrecordsel.action";
	//意见反馈
	public static final String PERSONAL_VALUE_TYPE_FEEDBACK = BASICURL +"opinadd.action";
	//发表评论
	public static final String PERSONAL_VALUE_TYPE_EVALUATION = BASICURL +"evalsave.action";
	//--------------------------------------------------
	
	//webview加载文档
	public static final String WEBVIEW_VALUE_TYPE_TERM = BASICURL + "files/term.html";  //用户协议
	public static final String WEBVIEW_VALUE_TYPE_PAY_HELP = BASICURL + "files/pay_help.html";  //支付帮助
	public static final String WEBVIEW_VALUE_TYPE_ABOUT = BASICURL + "files/about.html";  //关于我们
	public static final String WEBVIEW_VALUE_TYPE_CONTACTUS = BASICURL + "files/contact_us.html";  //联系我们
}
