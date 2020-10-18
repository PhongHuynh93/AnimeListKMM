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
                        VStack(alignment: .leading) {
                            Text((item as! MangaListUI).title).padding(.top, 8)
                            ScrollView(.horizontal, content: {
                                HStack(spacing: 16) {
                                    ForEach((item as! MangaListUI).mangaList) { mangaItem in
                                        MangaView(mangaUI: mangaItem)
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
