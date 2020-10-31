//
//  HomeMangaView.swift
//  iosApp
//
//  Created by Coder on 10/26/20.
//  Copyright © 2020 orgName. All rights reserved.
//

import SwiftUI
import shared

struct HomeMangaView: View {
    @ObservedObject var homeVM = HomeMangaVM(homeVM: HomeMangaViewModel())
    
    var body: some View {
        NavigationView {
            List(homeVM.homeList) { home in
                let item = home.home
                switch item {
                case is MangaListUI:
                    MyVStack(alignment: .leading) {
                        NavigationLink(destination: LazyView(MangaListView(mangaListUI: item as! MangaListUI))) {
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
                        }).onAppear {
                            let itemId = home.id
                            homeVM.onItemAppear(itemId: itemId)
                        }
                        
                    }
                default:
                    Text("")
                }
                
            }
            .navigationBarTitle("Manga")
            .listSeparatorStyle(style: .singleLine)
        }
    }
}
