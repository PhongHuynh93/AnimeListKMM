import SwiftUI
import shared

struct ContentView: View {
    var body: some View {
        TabView {
            HomeMangaView()
                .tabItem {
                    Image(systemName: "heart.fill")
                    Text("Manga")
                }
            HomeAnimeView()
                .tabItem {
                    Image(systemName: "heart.fill")
                    Text("Anime")
                }
        }
    }
}
