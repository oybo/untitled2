package com.qkl.online.mining.app.mvp.view;

import com.qkl.online.mining.app.data.entity.BaseBean;
import com.qkl.online.mining.app.data.entity.MyStar;
import com.qkl.online.mining.app.data.entity.StarProduct;

/**
 *
 * @param <T>
 */

public interface IStarView<T extends BaseBean> extends IBaseView {

    void resultMine(StarProduct starProduct);

    void resultMyStar(MyStar myStar);

    void resultStarRestNumber(int number);

    void resultPurchaseStar();

    void resultOrderConfig(int minAmount, int maxAmount, boolean canBuy, String minAmountFormat, String maxAmountFormat);

    void resultOrderWalletStat(int reinvestAmount, int yunAmount);

    void showLoading();

    void hideLoading();

}
