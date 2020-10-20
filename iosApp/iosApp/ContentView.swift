import SwiftUI
import shared

struct ContentView: View {
    @ObservedObject var homeVM = HomeVM(homeVM: HomeViewModel())
    
    var body: some View {
        NavigationView {
            List(homeVM.homeList) { home in
                let item = home.home
                switch item {
                case is MangaListUI:
                    MyVStack(alignment: .leading) {
                        NavigationLink(destination: LazyView(MangaListView())) {
                            Text((item as! MangaListUI).title).padding(EdgeInsets(top: 4, leading: 0, bottom: 4, trailing: 0))
                        }
                        ScrollView(.horizontal, content: {
                            MyHStack(spacing: 16) {
                                ForEach((item as! MangaListUI).mangaList) { mangaItem in
                                    NavigationLink(destination: LazyView(MangaDetailView(manga: mangaItem.manga))) {
                                        MangaView(mangaUI: mangaItem)
                                    }
                                }
                            }
                            
                        }).padding(.bottom, 12)
                        .frame(height: 160)
                    }
                default:
                    Text("")
                }
                
            }
            .navigationBarTitle("Browse")
        }
    }
}
