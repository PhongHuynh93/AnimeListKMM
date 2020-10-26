import SwiftUI
import shared

struct ContentView: View {
    @ObservedObject var homeVM = HomeVM(homeVM: HomeMangaViewModel())
    
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
                        let mangaChunkList = (item as! MangaListUI).mangaList.chunked(into: 2)
                        ScrollView(.horizontal, content: {
                            MyHStack {
                                ForEach(0..<mangaChunkList.count) { index in
                                    MyVStack(spacing: 10) {
                                        ForEach(mangaChunkList[index]) { mangaItem in
                                            NavigationLink(destination: LazyView(MangaDetailView(manga: mangaItem.manga))) {
                                                MangaView(mangaUI: mangaItem)
                                            }
                                        }.frame(height: 160)
                                    }
                                }.padding(.bottom, 10)
                                
                            }

//                            MyHStack(spacing: 10) {
//                                ForEach(mangaChunkList) { mangaItem in
//                                    NavigationLink(destination: LazyView(MangaDetailView(manga: mangaItem.manga))) {
//                                        MangaView(mangaUI: mangaItem)
//                                    }
//                                }
//                            }
                            
                        })
                        
                    }
                default:
                    Text("")
                }
                
            }
            .navigationBarTitle("Manga")
        }
    }
}
