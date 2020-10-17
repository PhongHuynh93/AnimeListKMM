import SwiftUI
import shared

struct ContentView: View {
    @ObservedObject var homeVM = HomeVM(homeVM: HomeViewModel())
   
    var body: some View {
            NavigationView {
                List(homeVM.homeList) { home in
                    let item = home.home
                    switch item {
//                    case is Divider:
//                        Text("must be divider be the same name in ios class")
                    case is MangaListUI:
                        ScrollView(.horizontal, content: {
                            HStack(spacing: 16) {
                                ForEach((item as! MangaListUI).mangaList) { mangaItem in
                                    MangaView(mangaUI: mangaItem)
                                }
                            }
                        })
                        .frame(height: 160)
                        // todo
                    case is Title:
                        Text((item as! Title).text)
                        // todo
                    default:
                        Text("divider")
                    }
            
                }
                .navigationBarTitle("Browse")
            }
        }
}
