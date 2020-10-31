//
//  HomeAnimeView.swift
//  iosApp
//
//  Created by Coder on 10/26/20.
//  Copyright © 2020 orgName. All rights reserved.
//

import SwiftUI
import shared

struct HomeAnimeView: View {
    @ObservedObject var homeVM = HomeAnimeVM(homeVM: HomeAnimeViewModel())
    
    var body: some View {
        NavigationView {
            List(homeVM.homeList) { item in
                let home = item.home
                switch home {
                case is AnimeListUI:
                    MyVStack(alignment: .leading) {
//                        NavigationLink(destination: LazyView(MangaListView())) {
                            Text((home as! AnimeListUI).title).padding(EdgeInsets(top: 4, leading: 0, bottom: 4, trailing: 0))
//                        }
                        let chunkList = (home as! AnimeListUI).list.chunked(into: 2)
                        ScrollView(.horizontal, content: {
                            MyHStack {
                                ForEach(0..<chunkList.count) { index in
                                    MyVStack(spacing: 10) {
                                        ForEach(chunkList[index]) { animeItem in
//                                            NavigationLink(destination: LazyView(MangaDetailView(manga: mangaItem.manga))) {
                                                AnimeView(item: animeItem)
//                                            }
                                        }.frame(height: 160)
                                    }
                                }.padding(.bottom, 10)
                                
                            }
                        }).onAppear {
                            let itemId = item.id
                            homeVM.onItemAppear(itemId: itemId)
                        }
                        
                    }
                default:
                    Text("")
                }
                
            }
            .navigationBarTitle("Anime")
            .listSeparatorStyle(style: .singleLine)
        }
    }
}
