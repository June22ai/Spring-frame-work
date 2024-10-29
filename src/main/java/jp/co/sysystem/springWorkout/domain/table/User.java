package jp.co.sysystem.springWorkout.domain.table;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import lombok.Data;

/**
 * Spring-data-jdbcで使用するエンティティ定義<br>
 * USERテーブル定義
 * エンティティ定義⇒データベースのレコードを表現するJavaクラス
 * @version 1.0.0 : 2020/05/13 新規作成
 */
@Data
public class User implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  private String Id;
  private String Pass;
  private String Name;
  private String Kana;
//  private String Birth;
//  private String Club; 
}
