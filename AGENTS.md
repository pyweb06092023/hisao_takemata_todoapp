# プロジェクト構成

このプロジェクトでは、以下の対応関係を前提に調査・変更する。

- 画面：
  `src/main/resources/templates/`
- 見た目：
  `src/main/resources/static/css/style.css`
- テーブル：
  `todos`
  初期定義は `initdb/01_create_table.sql`
- DB操作：
  `src/main/resources/mapper/TodoMapper.xml`
- API：
  `/api/todos`、`/api/holidays`
- API実装：
  `src/main/java/com/example/todoapp/api/`
- MCP道具：
  `src/main/java/com/example/todoapp/mcp/TodoTools.java`
- MCP入口：
  `/mcp`

# 作業ルール

- 作業前に関連ファイルを調査する。
- 変更対象のファイルと理由を説明する。
- 指示されていないファイルは変更しない。
- 画面変更では、API・DB・MCP仕様を変更しない。
- データ項目変更では、Javaモデル、Mapper、SQL、DTO/APIの整合性を確認する。
- 作業後にテストまたはビルドで確認する。
