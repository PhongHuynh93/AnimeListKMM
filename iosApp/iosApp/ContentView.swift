import SwiftUI
import shared

struct ContentView: View {
    var body: some View {
        TabView {
            LazyView(HomeMangaView())
                .tabItem {
                    Image(systemName: "heart.fill")
                    Text("Manga")
                }
            LazyView(HomeAnimeView())
                .tabItem {
                    Image(systemName: "heart.fill")
                    Text("Anime")
                }
        }
    }
}
