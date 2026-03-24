use tokio;

async fn example() {
    tokio::spawn(async { do_work() });
    tokio::spawn_blocking(|| heavy_computation());
    tokio::task::spawn_local(async { local_work() });
}
