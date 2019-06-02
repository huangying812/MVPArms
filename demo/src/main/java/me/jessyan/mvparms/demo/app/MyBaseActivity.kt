package me.jessyan.mvparms.demo.app

import com.jess.arms.base.BaseActivity
import com.jess.arms.mvp.IPresenter

/**
 * Author : zhongwenpeng
 * Email : 1340751953@qq.com
 * Time :  2019/1/9
 * Description :
 */
abstract  class MyBaseActivity<P:IPresenter>: BaseActivity<P>()