# DB設計書

## 1. この設計書の目的

この設計書は、`requirements.md` と `handout_app_spec.md` をもとに、ToDoアプリで使うデータベース（DB: データを保存する箱）の設計をまとめたものです。
このアプリで使うテーブル（表のような保存単位）は `todos` の 1 つだけです。

## 2. 前提

- DBは MySQL を前提とします。
- テーブルは `todos` の 1 つだけです。
- 画面設計は別資料で作るため、この資料では保存するデータの形に集中します。
- ログイン機能はなく、利用者は 1 人想定です。

## 3. 要件から見た保存対象

要件から、1件のToDoに対して次の情報を保存する必要があります。

- `id`: ToDoを見分ける番号
- `title`: やることのタイトル
- `detail`: 詳細メモ
- `category`: ジャンル
- `priority`: 優先度
- `due_date`: 期限日
- `completed`: 完了したかどうか
- `created_at`: 作成日時
- `updated_at`: 更新日時

## 4. テーブル一覧

| テーブル名 | 用途 |
|---|---|
| `todos` | ToDo 1件分の情報を保存する |

## 5. テーブル詳細

### 5.1 `todos` テーブル定義

| カラム名 | 意味 | 型 | 必須 | 初期値 | 制約・補足 |
|---|---|---|---|---|---|
| `id` | ToDoを一意に識別する番号 | `BIGINT` | 必須 | 自動採番 | 主キー（行を一意に決める列） |
| `title` | やること | `VARCHAR(255)` | 必須 | なし | 1〜255文字 |
| `detail` | 詳細メモ | `VARCHAR(255)` | 任意 | `NULL` | 0〜255文字 |
| `category` | ジャンル | `VARCHAR(255)` | 必須 | なし | 指定された5種類のみ |
| `priority` | 優先度 | `INT` | 必須 | `2` | `1:高 / 2:中 / 3:低` |
| `due_date` | 期限日 | `DATE` | 任意 | `NULL` | 日付のみを保存 |
| `completed` | 完了状態 | `BOOLEAN` | 必須 | `FALSE` | `TRUE:完了 / FALSE:未完了` |
| `created_at` | 作成日時 | `DATETIME` | 必須 | 自動設定 | 登録時刻 |
| `updated_at` | 更新日時 | `DATETIME` | 必須 | 自動設定 | 更新時刻 |

### 5.2 `category` の許可値

`category` には次の 5 つだけを保存します。

- `デザイン`
- `マーケティング`
- `プログラミング`
- `資格`
- `就職活動`

### 5.3 `priority` の表現

画面では文字で見せますが、DBでは数値で保存します。

| 保存値 | 画面表示 |
|---|---|
| `1` | 高 |
| `2` | 中 |
| `3` | 低 |

### 5.4 `completed` の表現

完了状態は真偽値（しんぎち: はい/いいえの値）で保存します。

| 保存値 | 意味 |
|---|---|
| `TRUE` | 完了 |
| `FALSE` | 未完了 |

## 6. CREATE TABLE 文（DDL）

DDL（ディーディーエル: テーブルを作るためのSQL）は次のとおりです。

```sql
CREATE TABLE todos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    detail VARCHAR(255) NULL,
    category VARCHAR(255) NOT NULL,
    priority INT NOT NULL DEFAULT 2,
    due_date DATE NULL,
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT chk_todos_category
        CHECK (category IN ('デザイン', 'マーケティング', 'プログラミング', '資格', '就職活動')),
    CONSTRAINT chk_todos_priority
        CHECK (priority IN (1, 2, 3)),
    CONSTRAINT chk_todos_title_length
        CHECK (CHAR_LENGTH(title) BETWEEN 1 AND 255),
    CONSTRAINT chk_todos_detail_length
        CHECK (detail IS NULL OR CHAR_LENGTH(detail) <= 255)
);
```

## 7. 要件とカラムの対応

どの要件がどのカラムに対応するかを、分かるように整理します。

| 要件 | 対応カラム | 対応内容 |
|---|---|---|
| ToDoを新規登録できる | `title`, `detail`, `category`, `priority`, `due_date`, `created_at`, `updated_at` | 登録時に必要な内容を保存する |
| ToDo一覧を表示できる | `id`, `title`, `category`, `priority`, `due_date`, `completed` | 一覧画面に必要な項目を表示する |
| ToDoを編集できる | `id`, `title`, `detail`, `category`, `priority`, `due_date`, `completed`, `updated_at` | 指定したToDoを更新する |
| ToDoを削除できる | `id` | 対象行を特定して削除する |
| キーワード検索ができる | `title` | 要件より `title` を部分一致で検索する |
| カテゴリ検索ができる | `category` | 選択したカテゴリで絞り込む |
| 「すべて」を選んだらカテゴリ検索しない | `category` | DBには保存せず、検索条件の出し分けで対応する |
| 期限日で並び替えできる | `due_date` | 昇順・降順で並び替える |
| 優先度を「高・中・低」で扱える | `priority` | DBでは `1,2,3` で保存する |
| 完了/未完了を保持できる | `completed` | 完了チェック状態を保存する |
| 作成日時・更新日時を管理できる | `created_at`, `updated_at` | 登録日時と更新日時を保存する |

## 8. 入力チェックとDB制約の対応

入力チェック（にゅうりょくチェック: 入れた値が正しいか確認すること）と、DB側の制約（せいやく: 保存できる値のルール）の対応は次のとおりです。

| 入力ルール | DBでの表現 |
|---|---|
| `title` は必須 | `title NOT NULL` |
| `title` は255文字以内 | `VARCHAR(255)` と `CHECK` |
| `detail` は255文字以内 | `VARCHAR(255)` と `CHECK` |
| `category` は必須 | `category NOT NULL` |
| `category` は5種類から選択 | `CHECK (category IN (...))` |
| `priority` は必須 | `priority NOT NULL` |
| `priority` は高・中・低のみ | `CHECK (priority IN (1,2,3))` |
| `due_date` は任意 | `due_date NULL` |
| `completed` は完了/未完了のみ | `BOOLEAN` |

## 9. 検索・並び替えで使うカラム

検索や並び替え（ならびかえ: 順番を変えること）で使うカラムは次のとおりです。

| 機能 | 使用カラム | 使い方 |
|---|---|---|
| キーワード検索 | `title` | 部分一致検索 |
| カテゴリ絞り込み | `category` | 完全一致検索 |
| 期限日昇順 | `due_date` | 古い日付から並べる |
| 期限日降順 | `due_date` | 新しい日付から並べる |

## 10. 補足

- `detail` と `due_date` は未入力を許可するため `NULL` を使います。`NULL` は「空っぽ」を表す値です。
- `created_at` と `updated_at` は MySQL の機能で自動設定する想定です。
- `completed` は一覧表示では「完了 / 未完了」の文字に変換して見せます。
- `priority` は一覧表示では「高 / 中 / 低」の文字に変換して見せます。
