async fn nested_closures() {
    let closure = || async {
        some_future().await;
    };

    let result = closure().await;
}

fn not_async_at_all() {
    let x = 42;
    println!("{}", x);
}
