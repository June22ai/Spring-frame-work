package jp.co.sysystem.springWorkout.web.controller.page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sysystem.springWorkout.service.LoginService;
import jp.co.sysystem.springWorkout.util.MessageUtil;
import jp.co.sysystem.springWorkout.web.form.LoginForm;
import jp.co.sysystem.springWorkout.web.form.SearchForm;
import lombok.extern.slf4j.Slf4j;

/**
 * ログイン画面コントローラー
 * @version 1.0.0 2020/05/13 新規作成
 */
@Controller
@EnableAutoConfiguration
@Slf4j
public class LoginController {
  //準備
  @Autowired
  HttpSession session;

  @Autowired
  public MessageUtil msgutil;

  @Autowired
  public LoginService login;

  /// URL定義
  public static final String LOGIN_FORM_URL = "/";
  public static final String LOGIN_PROCESS_URL = "/login";
  public static final String LOGOUT_URL = "/logout";

  ///ページ定義
  public static final String LOGIN_PAGE = "page/login";

  /**
   * ログイン画面表示
   * ログイン前でも表示可能
   */
  @RequestMapping(value = LOGIN_FORM_URL, method = RequestMethod.GET)
  public String showLoginPage(HttpServletRequest request, Model model) {
    // ログインフォームを格納new LoginForm()を作成して、ログインフォームを格納する
    model.addAttribute("loginForm", new LoginForm());
    // model.addAttributeでどういう仕組みで何が行われているか理解する
    return LOGIN_PAGE;
    //login.htmlが表示される
  }

  /**
   * ログイン処理<br>
   * ログイン成功である場合、メニュー画面へリダイレクトする。
   * @param form
   * @param bindingResult
   * @param model
   * @return
   */
  @RequestMapping(value = LOGIN_PROCESS_URL, method = RequestMethod.POST)
  public String processLogin(
      //入力チェックを行いたい引数LoginFormオブジェクトの前に@Validatedを設定
      @Validated @ModelAttribute LoginForm form,
      //入力チェック(Validation)の結果は、BindingResultオブジェクトに格納。
      //processLoginメソッドの引数として渡される。
      BindingResult bindingResult,
      Model model) {

    //bindingResultオブジェクトを使って入力チェックの結果確認
    //Validation：入力内容や記述内容が要件を満たしているか妥当性をチェック
    if (bindingResult.hasErrors()) {
      // TODO: ログインフォームからの入力値確認結果にエラーがある場合の処理
      // エラーメッセージをリソースファイルから取得
      String msg = msgutil.getMessage("login.failure");
      log.debug(msg);
      // エラーメッセージを画面に表示する
      model.addAttribute("msg", msg);

      // ログインに失敗したら、もう一度ログイン画面を表示
      // ログインフォームを格納
      model.addAttribute("loginForm", form);
      return LOGIN_PAGE;
    }

    // ログインユーザー情報の正当性判定
    if (null == login.checkLoginUser(form.getId(), form.getPassword())) {
      // エラーメッセージをリソースファイルから取得
      String msg = msgutil.getMessage("login.failure");
      log.debug(msg);
      // エラーメッセージを画面に表示する
      //Thymeleafを導入したHTMLにサーバーから送ったデータを表示させるには、
      //Spring FrameworkのModelクラスにあるaddAttributeメソッドを使用する
      model.addAttribute("msg", msg);

      // ログインフォームを格納(ログインに失敗した場合)
      model.addAttribute("loginForm", form);
      return LOGIN_PAGE;
    }

    //ログインフォームをセッションに保存
    session.setAttribute("loginForm", form);
    //サーチフォームを格納 search.htmlにあるth:object="${searchForm}"で利用
    //new SearchForm())⇒何も入っていない空のSearchFormを作っている
    model.addAttribute("searchForm", new SearchForm());
    // ログインフォームを格納(ログイン成功時の処理)
    model.addAttribute("loginForm", new LoginForm());
    //遷移先画面の指定
    return "page/search";
  }

  /**
   * ログアウト
   * @param model
   * @return
   */
  @RequestMapping(value = LOGOUT_URL, method = RequestMethod.GET)
  public String logout(Model model) {
    // 既存セッションを削除
    session.invalidate();
    // 遷移先のログイン画面で使用する空のForm
    model.addAttribute("loginForm", new LoginForm());
    return LOGIN_PAGE;
  }
}
