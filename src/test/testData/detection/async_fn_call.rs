async fn fetch_data() -> String {
    String::from("data")
}

fn sync_fn() -> i32 {
    42
}

async fn caller() {
    let data = fetch_data().await;
    let num = sync_fn();
}
