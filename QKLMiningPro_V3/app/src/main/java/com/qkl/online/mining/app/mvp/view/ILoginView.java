package com.qkl.online.mining.app.mvp.view;

import com.qkl.online.mining.app.data.entity.BaseBean;
import com.qkl.online.mining.app.data.entity.Deposit;

/**
 *
 * @param <T>
 */

public interface ILoginView<T extends BaseBean> extends IBaseView {

    void showLoading();

    void hideLoading();

    void loginSuccess(Deposit deposit);

}
