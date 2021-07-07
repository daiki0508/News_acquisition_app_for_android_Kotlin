cmake_minimum_required(VERSION 3.4.1)

add_library(
  # 識別用ライブラリ名を指定
  main

  # 共有ライブラリとしてビルドさせる
  SHARED

  # C/C++ソースへの相対パス指定
        main.cpp
)

target_link_libraries(
        main
        android
        log
)